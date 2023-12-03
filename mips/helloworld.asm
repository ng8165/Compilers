# HELLO WORLD
# This hello world program loads the "Hello World!" text from memory and loads it
# into a register. It then prints the message before terminating normally.
#
# @author Nelson Gou
# @version 11/7/23

.data
	msg: .asciiz "Hello World!\n"

.text

main:
	# print msg
	li $v0, 4
	la $a0, msg
	syscall
	
	# normal termination
	li $v0, 10
	syscall
