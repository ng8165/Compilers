# NEWLISTNODE and SUMLIST
# The newlistnode subroutine allocates heap space to store a value and a next address.
# The sumlist subroutine takes a listnode address and adds values until the listnode reaches null.
#
# @author Nelson Gou
# @version 11/28/23

.data

.text

main:
	# newlistnode(3, null) caller
	li $a0 3
	li $a1 0
	jal newlistnode
	
	# newlistnode(2, node3) caller
	li $a0 2
	move $a1 $v0
	jal newlistnode
	
	# newlistnode(1, node2) caller
	li $a0 1
	move $a1 $v0
	jal newlistnode	# at this point, $v0 stores a linked list of 1 --> 2 --> 3 --> null
	
	# sumlist(node1) caller
	move $a0 $v0
	jal sumlist
	move $a0 $v0
	li $v0 1
	syscall
	
	# normal termination
	li $v0 10
	syscall

newlistnode:
	# newlistnode(value, next) callee
	move $t0 $a0
	li $v0 9	# allocate memory
	li $a0 8	# 8 bytes of memory
	syscall
	sw $t0 ($v0)	# store value
	sw $a1 4($v0)	# store next
	jr $ra		# return memory address in $v0

sumlist:
	# sumlist(list) callee
	subu $sp $sp 8
	sw $ra ($sp)	# store $ra in stack
	sw $s0 4($sp)	# store value in stack
	
	bnez $a0 else
	
	# list is null
	li $v0 0
	j sumlistdone
	
else:	# list is not null
	lw $s0 ($a0)	# $s0 is the value
	lw $a0 4($a0)	# $a0 is the next
	jal sumlist
	addu $v0 $s0 $v0	# add value and sumlist(next)

sumlistdone:
	lw $ra ($sp)	# load $ra from stack
	lw $s0 4($sp)	# load value in stack
	addu $sp $sp 8
	
	jr $ra