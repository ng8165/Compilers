# MULTIPLICATION (EXERCISE 4)
# This program prompts the user for two numbers and prints out their product.
#
# @author Nelson Gou
# @version 11/7/23

.data
	msg1: .asciiz "Enter a number: "
	msg2: .asciiz "Enter another number: "
	msg3: .asciiz "Multiplication result is: "
	
.text

main:
	# print 1st prompt (msg1)
	li $v0, 4
	la $a0, msg1
	syscall
	
	# read 1st number and store in $t0
	li $v0, 5 	
	syscall
	move $t0, $v0
	
	# print 2nd prompt (msg2)
	li $v0, 4
	la $a0, msg2
	syscall
	
	# read 2nd number. stored in $v0
	li $v0, 5
	syscall
	
	# multiply numbers and store product in $t0
	mult $t0, $v0
	mflo $t0
	
	# print result text (msg3)
	li $v0 4
	la $a0 msg3
	syscall
	
	# print result of multiplication. stored in $t0
	li $v0 1
	move $a0, $t0
	syscall
	
	# normal termination
	li $v0 10
	syscall
