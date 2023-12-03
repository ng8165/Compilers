# FIB
# Recursively calculates the nth Fibonacci number. Takes in a number n from user input.
#
# @author Nelson Gou
# @version 11/18/23

.data
	prompt: .asciiz "Enter an integer n: "
	resultTxt: .asciiz "The nth Fibonacci number is "
	n: .word 0
	result: .word 0

.text

main:
	# print prompt and take user input (into $a0)
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	sw $v0 n
	
	# fib(n) caller
	lw $a0 n
	jal fib
	sw $v0 result	# save result into $t0
	
	# print result text and result number
	li $v0 4
	la $a0 resultTxt
	syscall
	li $v0 1
	lw $a0 result
	syscall
	
	# normal termination
	li $v0 10
	syscall
	
fib:
	# fib(n) callee
	subu $sp $sp 12
	sw $ra ($sp)	# save $ra to stack
	sw $s0 4($sp)	# save $s0 to stack
	sw $s1 8($sp)	# save $s1 to stack
	
	bgt $a0 1 else
	
	# n <= 1
	move $v0 $a0
	j fibdone
	
else:	# n > 1
	move $s0 $a0	# save n into $s0
	
	# fib(n-1) caller
	subu $a0 $s0 1	# calculate n-1
	jal fib
	move $s1 $v0	# save fib(n-1) in $s1
	
	# fib(n-2) caller
	subu $a0 $s0 2	# calculate n-2
	jal fib
	
	# calculate result: fib(n-1) + fib(n-2)
	addu $v0 $s1 $v0

fibdone:
	lw $ra ($sp)	# load $ra from stack
	lw $s0 4($sp)	# load $s0 from stack
	lw $s1 8($sp)	# load $s1 from stack
	addu $sp $sp 12
	
	jr $ra
