# EVEN OR ODD (EXERCISE 5)
# This program reads an integer from user input. It then prints "Even" if the number
# is even and "Odd" if the number is odd.
#
# @author Nelson Gou
# @version 11/7/23

.data
	prompt: .asciiz "Enter a number: "
	even: .asciiz "Even"
	odd: .asciiz "Odd"

.text

main:
	# print prompt
	li $v0, 4
	la $a0, prompt
	syscall
	
	# take user input, stored in $v0
	li $v0, 5
	syscall
	
	# divide user input ($v0) by 2 and store remainder in $t0
	li $t0, 2
	div $v0, $t0
	mfhi $t0 # mfhi returns the remainder
	
	# if remainder ($t0) is 0, jump to printEven
	beqz $t0, printEven
	
	# print odd text (only runs if remainder $t0 is 1)
	li $v0, 4
	la $a0, odd
	syscall
	
	j after # jump to after
	
printEven:
	# print even text. then, automatically proceeds to after.
	li $v0, 4
	la $a0, even
	syscall

after:
	# normal termination
	li $v0, 10
	syscall
