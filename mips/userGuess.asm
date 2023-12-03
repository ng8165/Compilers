# GUESS A NUMBER, USER EDITION
# The program generates a random number from 1 to 100 and then repeatedly asks the user to guess
# the number. It then tells the user if the guess was too low, too high, or correct. If the guess
# is correct, the program terminates normally.
#
# @author Nelson Gou
# @version 11/7/23

.data
	intro: .asciiz "I will choose a number from 1 to 100. You can guess what it is, and I will tell you if it is too high or too low. You win once you guess correctly. Let's begin!\n"
	prompt: .asciiz "Guess a number: "
	low: .asciiz "That's too low. Try again.\n"
	high: .asciiz "That's too high. Try again.\n"
	correct: .asciiz "You got it!"

.text

main:
	# print intro text
	li $v0, 4
	la $a0, intro
	syscall
	
	# choose random number and save in $t0
	li $v0, 42		# syscall 42 returns a random number from 0 <= rand < $a1
	li $a1, 100		# $a1 is the max bound of random number. set to 100
	syscall			# after syscall, the random number is saved in $a0
	addu $t0, $a0, 1	# add 1 to make random num from 1 <= rand <= $a1, and store random number in $t0
	
loop:
	# print prompt
	li $v0, 4
	la $a0, prompt
	syscall
	
	# read user input (stored in $v0)
	li $v0, 5
	syscall
	
	blt $v0, $t0, printLow	# if user input < random number, jump to printLow
	bgt $v0, $t0, printHigh	# if user input > random number, jump to printHigh
	
	# not too low or too high, so must be correct. print correct
	li $v0, 4
	la $a0, correct
	syscall
	
	# normal termination
	li $v0, 10
	syscall
	
printLow:
	# print low
	li $v0, 4
	la $a0, low
	syscall
	
	j loop # continue looping

printHigh:
	# print high
	li $v0, 4
	la $a0, high
	syscall
	
	j loop # continue looping
