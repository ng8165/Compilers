# OUTPUT.ASM
# This is automatically generated code. Do not modify.
# @author Nelson Gou
# @version 01/08/2024

	.data
newline: .asciiz "\n"
true: .asciiz "TRUE"
false: .asciiz "FALSE"
readBuffer: .space 129
str0: .asciiz "\n     A Basic Program"
str1: .asciiz "     ---------------"
str2: .asciiz " a) Celsius to Fahrenheit"
str3: .asciiz " b) Fahrenheit to Celsius"
str4: .asciiz " x) To exit the program\n"
str5: .asciiz "Enter your choice: "
str6: .asciiz "Enter number to convert: "
str7: .asciiz "Press RETURN to continue: "
str8: .asciiz "a"
str9: .asciiz "b"
str10: .asciiz "\n        Degree"
str11: .asciiz "Celsius    | Fahrenheit"
str12: .asciiz "Fahrenheit |   Celsius"
str13: .asciiz "------------------------"
str14: .asciiz "   |   "
str15: .asciiz "."
str16: .asciiz "\n"
str17: .asciiz "q"
str18: .asciiz "x"
varUserChoice: .word 0
varUserInput: .word 0
varanswer: .word 0
vardecimal: .word 0

	.text
	.globl main
main:
	la $v0 str17	# load string into accumulator
	sw $v0 varUserChoice	# load accumulator into UserChoice
while0:
contwhile0:
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str18	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	bnez $v0 endwhile0	# jump to endwhile0 if condition is not satisfied
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procShowTheMenu
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procGetUserChoice
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str8	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str9	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	or $v0 $t0 $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 -1	# load TRUE into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	bne $t0 $v0 endif1	# jump to endif1 if condition is not satisfied
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procGetNumberToConvert
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procDoTheConversion
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procDisplayTheAnswer
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procWait
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
endif1:
	j while0	# continue looping
endwhile0:
terminate:
	li $v0 10	# normal termination
	syscall

procShowTheMenu:
	la $v0 str0	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $v0 str1	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $v0 str2	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $v0 str3	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	la $v0 str4	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	jr $ra
procGetUserChoice:
	la $v0 str5	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal readstr	# get user string input in $v0
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	sw $v0 varUserChoice	# load accumulator into UserChoice
	jr $ra
procGetNumberToConvert:
	la $v0 str6	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 5	# get user integer input and store in UserInput
	syscall
	sw $v0 varUserInput	# load accumulator into UserInput
	jr $ra
procWait:
	la $v0 str7	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 8	# read string from user input
	la $a0 readBuffer
	li $a1 128	# max length of inputted string
	syscall
	jr $ra
procToFahrenheit:
	lw $v0 8($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	mult $t0 $v0
	mflo $v0	# multiply $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 9	# load integer 9 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	mult $t0 $v0
	mflo $v0	# multiply $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 5	# load integer 5 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 32	# load integer 32 into accumulator
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	mult $t0 $v0
	mflo $v0	# multiply $t0 and $v0
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	addu $v0 $t0 $v0	# add $t0 and $v0
	sw $v0 0($sp)	# save $v0 into local variable
	lw $v0 0($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	sw $v0 4($sp)	# save $v0 into local variable
	lw $v0 0($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mfhi $v0	# find remainder of $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 100	# load integer 100 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	sw $v0 vardecimal	# load accumulator into decimal
	lw $v0 vardecimal	# loads variable decimal into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 0	# load integer 0 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	bge $t0 $v0 endif4	# jump to endif4 if condition is not satisfied
	lw $v0 vardecimal	# loads variable decimal into $v0
	subu $v0 $zero $v0	# negate accumulator contents
	sw $v0 vardecimal	# load accumulator into decimal
endif4:
	jr $ra
procToCelsius:
	lw $v0 8($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 32	# load integer 32 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $v0 $t0 $v0	# subtract $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	mult $t0 $v0
	mflo $v0	# multiply $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 5	# load integer 5 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	mult $t0 $v0
	mflo $v0	# multiply $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 9	# load integer 9 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	sw $v0 0($sp)	# save $v0 into local variable
	lw $v0 0($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	sw $v0 4($sp)	# save $v0 into local variable
	lw $v0 0($sp)	# load local variable into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 10000	# load integer 10000 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mfhi $v0	# find remainder of $t0 and $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 100	# load integer 100 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	div $t0 $v0
	mflo $v0	#divide $t0 and $v0
	sw $v0 vardecimal	# load accumulator into decimal
	lw $v0 vardecimal	# loads variable decimal into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	li $v0 0	# load integer 0 into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	bge $t0 $v0 endif5	# jump to endif5 if condition is not satisfied
	lw $v0 vardecimal	# loads variable decimal into $v0
	subu $v0 $zero $v0	# negate accumulator contents
	sw $v0 vardecimal	# load accumulator into decimal
endif5:
	jr $ra
procDoTheConversion:
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str8	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	beqz $v0 endif6	# jump to endif6 if condition is not satisfied
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	lw $v0 varUserInput	# loads variable UserInput into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procToFahrenheit
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	sw $v0 varanswer	# load accumulator into answer
endif6:
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str9	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	beqz $v0 endif7	# jump to endif7 if condition is not satisfied
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	lw $v0 varUserInput	# loads variable UserInput into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	subu $sp $sp 4	# push $zero into stack
	sw $zero ($sp)
	jal procToCelsius
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	sw $v0 varanswer	# load accumulator into answer
endif7:
	jr $ra
procDisplayTheAnswer:
	la $v0 str10	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str8	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	beqz $v0 endif8	# jump to endif8 if condition is not satisfied
	la $v0 str11	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif8:
	lw $v0 varUserChoice	# loads variable UserChoice into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str9	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcmp
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	beqz $v0 endif9	# jump to endif9 if condition is not satisfied
	la $v0 str12	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
endif9:
	la $v0 str13	# load string into accumulator
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	lw $v0 varUserInput	# loads variable UserInput into $v0
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str14	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	move $a0 $t0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal intToStr	# convert integer to string
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	move $t0 $v0
	lw $v0 ($sp)	# pop stack into $v0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcat
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	lw $v0 varanswer	# loads variable answer into $v0
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $t0 into stack
	sw $t0 ($sp)
	move $a0 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal intToStr	# convert integer to string
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcat
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str15	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcat
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	lw $v0 vardecimal	# loads variable decimal into $v0
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	subu $sp $sp 4	# push $t0 into stack
	sw $t0 ($sp)
	move $a0 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal intToStr	# convert integer to string
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcat
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	subu $sp $sp 4	# push $v0 into stack
	sw $v0 ($sp)
	la $v0 str16	# load string into accumulator
	lw $t0 ($sp)	# pop stack into $t0
	addu $sp $sp 4
	move $a0 $t0
	move $a1 $v0
	subu $sp $sp 4	# push $ra into stack
	sw $ra ($sp)
	jal strcat
	lw $ra ($sp)	# pop stack into $ra
	addu $sp $sp 4
	move $a0 $v0	# print string
	li $v0 4
	syscall
	li $v0 4
	la $a0 newline
	syscall
	jr $ra
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
readbool:
	subu $sp $sp 4	# push $ra onto stack
	sw $ra ($sp)
	jal readstr	# read string and save in $s0
	move $s0 $v0
	move $a0 $s0
	la $a1 true
	jal strcmp	# compare string input and TRUE
	beq $v0 -1 readboolend
	move $a0 $s0
	la $a1 false
	jal strcmp	# compare string input and FALSE
	beq $v0 -1 readboolfalse
	break	# throw error if string is neither TRUE nor FALSE
readboolfalse:
	li $v0 0
readboolend:
	lw $ra ($sp)	# load $ra from stack
	addu $sp $sp 4
	jr $ra
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
