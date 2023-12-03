package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Program class represents a Pascal program, which is a list
 * of procedures and a Statement.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Program
{
    private final List<VariableDeclaration> variables;
    private final List<ProcedureDeclaration> procedures;
    private final List<String> strings;
    private final Statement stmt;

    /**
     * Instantiates the Program with a List of procedures and a Statement.
     * @param variables list of global variable names
     * @param strings list of all string literals
     * @param procedures list of procedures
     * @param stmt the Statement of the program
     */
    public Program(List<VariableDeclaration> variables, List<String> strings,
                   List<ProcedureDeclaration> procedures, Statement stmt)
    {
        this.variables = variables;
        this.strings = strings;
        this.procedures = procedures;
        this.stmt = stmt;
    }

    /**
     * Executes all ProcedureDeclarations (setting them in the Environment), then executes
     * the Statement of the program.
     * @param env the Environment
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration procedure: procedures)
            procedure.exec(env);

        stmt.exec(env);
    }

    /**
     * Emits the compiled Program in MIPS with the default file name "output.asm".
     */
    public void compile()
    {
        compile("output.asm");
    }

    /**
     * Emits the compiled Program in MIPS.
     * Loads global variables and strings into the .data section.
     * Places the Statement into the main label in the .text section and adds
     * normal termination code after that.
     * @param file the name of the output file
     */
    public void compile(String file)
    {
        Emitter e = new Emitter(file, variables);

        // documentation
        String currDate = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now());
        e.emit("""
                # %s
                # This is automatically generated code. Do not modify.
                # @author Nelson Gou
                # @version %s
                """.formatted(file.toUpperCase(), currDate), false);

        // .data section
        e.emit("""
                .data
                newline: .asciiz "\\n"
                true: .asciiz "TRUE"
                false: .asciiz "FALSE"
                readBuffer: .space 129""");

        // load all string literals into .data
        for (int i=0; i<strings.size(); i++)
            e.emit("str" + i + ": .asciiz \"" + strings.get(i) + "\"");

        // load all declared global variables into .data
        for (VariableDeclaration varDec: variables)
            e.emit("var" + varDec.getName() + ": .word 0");

        e.emit();
        e.emit("""
                .text
                \t.globl main
                main:""");

        stmt.compile(e);

        // normal termination
        e.emit("""
                terminate:
                \tli $v0 10\t# normal termination
                \tsyscall
                """, false);

        // helper function for converting booleans to strings
        e.emit("""
                boolToStr:
                \tbeq $a0 0 boolFalse
                \tla $v0 true\t# return 'TRUE'
                \tjr $ra
                boolFalse:
                \tla $v0 false\t# return 'FALSE'
                \tjr $ra""", false);

        // helper function for comparing string equality
        e.emit("""
                strcmp:
                \tlb $t0 ($a0)\t# get next character from first string
                \tlb $t1 ($a1)\t# get next character from second string
                \tbne $t0 $t1 strcmpFalse\t# if characters are not equal, return FALSE
                \tbeqz $t0 strcmpTrue\t# if both characters are null, return TRUE
                \taddu $a0 $a0 1\t# increment first string
                \taddu $a1 $a1 1\t# increment second string
                \tj strcmp
                strcmpFalse:
                \tli $v0 0\t# return FALSE
                \tjr $ra
                strcmpTrue:
                \tli $v0 -1\t# return TRUE
                \tjr $ra""", false);

        // helper function for getting string length
        e.emit("""
                strlen:
                \tli $v0 0\t# $v0 represents string length
                strlenLoop:
                \tlb $t0 ($a0)
                \tbeqz $t0 strlenEnd\t# if current character is null, the string is terminated
                \taddu $v0 $v0 1\t# increment string length
                \taddu $a0 $a0 1\t# get next character
                \tj strlenLoop
                strlenEnd:
                \tjr $ra\t# return string length""", false);

        // helper function for concatenating strings
        e.emit("""
                strcat:
                \tmove $s3 $ra\t# save $ra in safe register
                \tmove $s0 $a0\t# move strings to safe registers
                \tmove $s1 $a1
                \tjal strlen\t# get length of first string
                \tmove $s2 $v0\t# save strlen into a safe register
                \tmove $a0 $s1\t# get length of first string
                \tjal strlen
                \taddu $a0 $v0 $s2\t# find concatenated string length
                \taddu $a0 $a0 1\t# increment length to include null character
                \tli $v0 9
                \tsyscall\t# allocate heap space for concatenated string
                \tmove $s2 $v0\t# memory address in $s2 and $v0
                strcatLoop1:
                \tlb $t0 ($s0)\t# load current character of string 1
                \tbeqz $t0 strcatLoop2\t# if at end of string 1, jump to string 2
                \tsb $t0 ($s2)\t# store character in new string
                \taddu $s0 $s0 1\t# increment pointers for string 1 and new string
                \taddu $s2 $s2 1
                \tj strcatLoop1
                strcatLoop2:
                \tlb $t1 ($s1)\t# load current character of string 2
                \tbeqz $t1 strcatEnd\t# if at end of string 2, jump to end
                \tsb $t1 ($s2)\t# store character in new string
                \taddu $s1 $s1 1\t# increment pointers for string 2 and new string
                \taddu $s2 $s2 1
                \tj strcatLoop2
                strcatEnd:
                \tsb $zero ($s2)\t# null append the new string
                \tmove $a0 $v0\t# prepare for syscall 4 and printing
                \tjr $s3\t# new string address is still in $v0""", false);

        e.emit("""
                readstr:
                \tmove $s3 $ra\t# save return address in a safe register
                \tli $v0 8\t# read string from user input
                \tla $a0 readBuffer
                \tli $a1 128\t# max length of inputted string
                \tsyscall
                \tla $a0 readBuffer\t# find length of inputted string
                \tjal strlen
                \tmove $a0 $v0\t# allocate space for string
                \tli $v0 9
                \tsyscall
                \tmove $s0 $v0
                \tla $s1 readBuffer
                readstrcopy:
                \tlb $t0 ($s1)\t# load inputted character into $t0
                \tbeq $t0 10 readstrend\t# if at enter character, stop copying
                \tsb $t0 ($s0)\t# copy into allocated string
                \taddu $s0 $s0 1\t# increment allocated string and inputted string
                \taddu $s1 $s1 1
                \tj readstrcopy
                readstrend:
                \tsb $zero ($s0)\t# null-append allocated string
                \tjr $s3\t# $s3 contains $ra""", false);

        e.emit("""
                intToStr:
                \tmove $s0 $a0\t# set $s0 to the number
                \tabs $v0 $s0\t# let $v0 be absolute value of number
                \tli $t0 0\t# $t0 stores the number of digits in the number
                \tli $t1 10\t# $t1 is 10
                intToStrLoop:
                \tdiv $v0 $t1
                \tmfhi $t2\t# put last digit in $t2
                \taddu $t2 $t2 48\t# turn number into character ('0' is ASCII code 48)
                \tsubu $sp $sp 1\t# store the last digit on the stack
                \tsb $t2 ($sp)
                \tmflo $v0\t# remove last digit from number
                \taddu $t0 $t0 1\t# increment digit counter
                \tbeqz $v0 intToStrNeg\t# if no more digits, exit loop
                \tj intToStrLoop
                intToStrNeg:
                \tbgez $s0 intToStrGen
                \taddu $t0 $t0 1\t# if number is negative, increment length for hyphen
                \tsubu $sp $sp 1\t# add hyphen to stack
                \tli $t1 45\t# hyphen is ASCII code 45
                \tsb $t1 ($sp)
                intToStrGen:
                \taddu $a0 $t0 1\t# add character for null appended string
                \tli $v0 9
                \tsyscall\t# allocate heap space for string
                \tmove $s0 $v0
                intToStrGenLoop:
                \tbeqz $t0 intToStrFinish\t# if no more digits in number, finish
                \tlb $t1 ($sp)\t# load digit from stack
                \taddu $sp $sp 1
                \tsb $t1 ($s0)\t# store digit in string
                \taddu $s0 $s0 1\t# increment string to next byte
                \tsubu $t0 $t0 1\t# decrement digit counter
                \tj intToStrGenLoop
                intToStrFinish:
                \tsb $zero ($s0)\t# null append the string
                \tjr $ra\t# original string address is in $v0""", false);

        e.close();
    }
}
