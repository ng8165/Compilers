# MAX2
# Takes two integers from user input. Then uses a subroutine called max2,
# which takes in two integers and returns whichever is greater. Prints the result.
# @author Nelson Gou
# @version 11/17/23

.data
	prompt: .asciiz "Enter an integer: "
	result: .asciiz "The greater integer is "

.text

main:
	# take user input into $t0
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	move $t0 $v0
	
	# take user input into $v0
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	
	# max2(x, y) caller
	move $a0 $t0	# prepare arguments
	move $a1 $v0
	subu $sp $sp 4
	sw $ra ($sp)	# save $ra in stack
	jal max2
	lw $ra ($sp)	# load $ra from stack
	addu $sp $sp 4
	
	# print result
	move $t0 $v0
	li $v0 4
	la $a0 result
	syscall
	li $v0 1
	move $a0 $t0
	syscall
	
	# normal termination
	li $v0 10
	syscall
	
max2:
	# max2(x, y) callee
	
	ble $a0 $a1 else
	# x > y
	move $v0 $a0
	jr $ra
else:	# x <= y
	move $v0 $a1
	jr $ra