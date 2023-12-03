# FACT
# Uses a recursively written factorial subroutine (given a number n, returns n * fact(n-1)).
# Takes in a number n from user input.
# 
# @author Nelson Gou
# @version 11/17/23

.data
	prompt: .asciiz "Enter a nonnegative integer: "
	resultTxt: .asciiz "The factorial of that number is "
	n: .word 0
	result: .word 0

.text

main:
	# take user input into $a0
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	sw $v0 n
	
	# fact(n) caller
	lw $a0 n
	jal fact
	sw $v0 result
	
	# print factorial
	li $v0 4
	la $a0 resultTxt
	syscall
	li $v0 1
	lw $a0 result
	syscall
	
	# normal termination
	li $v0 10
	syscall

fact:
	# fact(n) callee
	subu $sp $sp 8
	sw $ra ($sp)	# save $ra to stack
	sw $s0 4($sp)
	
	bne $a0 0 else
	
	# n is equal to 0
	li $v0 1
	j factdone

else:	# n is not equal to 0
	move $s0 $a0	# move n to $s0
	
	# fact(n-1) caller
	subu $a0 $s0 1	# calculate n-1
	jal fact
	
	mult $s0 $v0	# mutliply n and fact(n-1)
	mflo $v0
	
factdone:
	lw $ra ($sp)	# load $ra from stack
	lw $s0 4($sp)
	addu $sp $sp 8	
	
	jr $ra
