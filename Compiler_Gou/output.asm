# OUTPUT.ASM
# This is automatically generated code. Do not modify.
# @author Nelson Gou
# @version 11/30/2023

	.data
newline: .asciiz "\n"
true: .asciiz "TRUE"
false: .asciiz "FALSE"
readBuffer: .space 129
str0: .asciiz "mips is the best thing to ever exist; it is "
str1: .asciiz "/"
str2: .asciiz ", and that statement is "
varstr: .word 0
varnum: .word 0

	.text
	.globl main
main:
	la $v0 str0	# load string into accumulator
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10	# load integer 10 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $t0 into stack
	sw $t0 ($sp)
	move $a0 $v0
	jal intToStr	# convert integer to string
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	jal strcat
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str1	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	jal strcat
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10	# load integer 10 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $t0 into stack
	sw $t0 ($sp)
	move $a0 $v0
	jal intToStr	# convert integer to string
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	jal strcat
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str2	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	jal strcat
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 -1	# load TRUE into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $t0 into stack
	sw $t0 ($sp)
	move $a0 $v0
	jal boolToStr	# convert bool to string
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	jal strcat
	sw $v0 varstr	# load varstr into accumulator
	lw $v0 varstr	# loads variable str into $v0
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
terminate:
	li $v0 10	# normal termination
	syscall

boolToStr:
	beq $a0 0 boolFalse
	la $v0 true	# return 'TRUE'
	jr $ra
boolFalse:
	la $v0 false	# return 'FALSE'
	jr $ra
strcmp:
	lb $t0 ($a0)	# get next character from first string
	lb $t1 ($a1)	# get next character from second string
	bne $t0 $t1 strcmpFalse	# if characters are not equal, return FALSE
	beqz $t0 strcmpTrue	# if both characters are null, return TRUE
	addu $a0 $a0 1	# increment first string
	addu $a1 $a1 1	# increment second string
	j strcmp
strcmpFalse:
	li $v0 0	# return FALSE
	jr $ra
strcmpTrue:
	li $v0 -1	# return TRUE
	jr $ra
strlen:
	li $v0 0	# $v0 represents string length
strlenLoop:
	lb $t0 ($a0)
	beqz $t0 strlenEnd	# if current character is null, the string is terminated
	addu $v0 $v0 1	# increment string length
	addu $a0 $a0 1	# get next character
	j strlenLoop
strlenEnd:
	jr $ra	# return string length
strcat:
	move $s3 $ra	# save $ra in safe register
	move $s0 $a0	# move strings to safe registers
	move $s1 $a1
	jal strlen	# get length of first string
	move $s2 $v0	# save strlen into a safe register
	move $a0 $s1	# get length of first string
	jal strlen
	addu $a0 $v0 $s2	# find concatenated string length
	addu $a0 $a0 1	# increment length to include null character
	li $v0 9
	syscall	# allocate heap space for concatenated string
	move $s2 $v0	# memory address in $s2 and $v0
strcatLoop1:
	lb $t0 ($s0)	# load current character of string 1
	beqz $t0 strcatLoop2	# if at end of string 1, jump to string 2
	sb $t0 ($s2)	# store character in new string
	addu $s0 $s0 1	# increment pointers for string 1 and new string
	addu $s2 $s2 1
	j strcatLoop1
strcatLoop2:
	lb $t1 ($s1)	# load current character of string 2
	beqz $t1 strcatEnd	# if at end of string 2, jump to end
	sb $t1 ($s2)	# store character in new string
	addu $s1 $s1 1	# increment pointers for string 2 and new string
	addu $s2 $s2 1
	j strcatLoop2
strcatEnd:
	sb $zero ($s2)	# null append the new string
	move $a0 $v0	# prepare for syscall 4 and printing
	jr $s3	# new string address is still in $v0
readstr:
	move $s3 $ra	# save return address in a safe register
	li $v0 8	# read string from user input
	la $a0 readBuffer
	li $a1 128	# max length of inputted string
	syscall
	la $a0 readBuffer	# find length of inputted string
	jal strlen
	move $a0 $v0	# allocate space for string
	li $v0 9
	syscall
	move $s0 $v0
	la $s1 readBuffer
readstrcopy:
	lb $t0 ($s1)	# load inputted character into $t0
	beq $t0 10 readstrend	# if at enter character, stop copying
	sb $t0 ($s0)	# copy into allocated string
	addu $s0 $s0 1	# increment allocated string and inputted string
	addu $s1 $s1 1
	j readstrcopy
readstrend:
	sb $zero ($s0)	# null-append allocated string
	jr $s3	# $s3 contains $ra
intToStr:
	move $s0 $a0	# set $s0 to the number
	abs $v0 $s0	# let $v0 be absolute value of number
	li $t0 0	# $t0 stores the number of digits in the number
	li $t1 10	# $t1 is 10
intToStrLoop:
	div $v0 $t1
	mfhi $t2	# put last digit in $t2
	addu $t2 $t2 48	# turn number into character ('0' is ASCII code 48)
	subu $sp $sp 1	# store the last digit on the stack
	sb $t2 ($sp)
	mflo $v0	# remove last digit from number
	addu $t0 $t0 1	# increment digit counter
	beqz $v0 intToStrNeg	# if no more digits, exit loop
	j intToStrLoop
intToStrNeg:
	bgez $s0 intToStrGen
	addu $t0 $t0 1	# if number is negative, increment length for hyphen
	subu $sp $sp 1	# add hyphen to stack
	li $t1 45	# hyphen is ASCII code 45
	sb $t1 ($sp)
intToStrGen:
	addu $a0 $t0 1	# add character for null appended string
	li $v0 9
	syscall	# allocate heap space for string
	move $s0 $v0
intToStrGenLoop:
	beqz $t0 intToStrFinish	# if no more digits in number, finish
	lb $t1 ($sp)	# load digit from stack
	addu $sp $sp 1
	sb $t1 ($s0)	# store digit in string
	addu $s0 $s0 1	# increment string to next byte
	subu $t0 $t0 1	# decrement digit counter
	j intToStrGenLoop
intToStrFinish:
	sb $zero ($s0)	# null append the string
	jr $ra	# original string address is in $v0
