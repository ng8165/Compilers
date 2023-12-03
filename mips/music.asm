# FELIZ NAVIDAD MUSIC
# This program plays the chorus of Feliz Navidad in C Major. The pitches and durations are hardcoded
# as arrays and are loaded and executed one by one.
#
# @author Nelson Gou
# @author https://musescore.com/user/24655541/scores/4814038
# @version 11/17/23

.data

# for reference: 60 = C4, 62 = D4, 64 = E4, 65 = F4, 67 = G4, 69 = A4, 71 = B4, 72 = C5, 74 = D5, -1 = rest
pitches:   .word 67, 72, 71, 72, 69, -1, 69, 74, 72, 69, 67, -1, 67, 72, 71, 72, 69, -1, 65, 69, 69, 67, 67, 67, 67, 65, 65, 64

# for reference: 1 denotes an eighth note, 2 denotes a quarter note, 3 denotes a dotted quarter note, 4 denotes a half note, etc.
durations: .word 1,  2,  1,  1,  4,  7,  1,  2,  1,  1,  4,  7,  1,  2,  1,  1,  2,  1,  2,  1,  1,  2,  1,  1,  1,  2,  1,  3

length:	   .word 28 # length of above two arrays

.text

main:
	li $a2, 5	# selects instrument as a piano
	li $a3, 127	# sets volume to maximum
	li $t3, 150	# number of milliseconds for an eighth note
	
	# load variables into registers ($t0 = pitches, $t1 = durations, $t2 = length)
	la $t0, pitches
	la $t1, durations
	lw $t2, length
	
loop:
	# once there are no more pitches, jump to end
	beqz $t2, end
	
	lw $a0, ($t0)	# load current pitch into $a0
	lw $a1, ($t1)	# load current duration into $a1
	mult $a1, $t3	# multiply by duration constant ($t3 = 150ms)
	mflo $a1	# load true duration into $a1
	
	bltz $a0, rest	# if the pitch is -1, jump to rest
	
	# otherwise, play the pitch
	li $v0, 33 # syscall 33 = synchronous MIDI output
	syscall
	
cont:	# move to the next pitch and duration
	addu $t0, $t0, 4
	addu $t1, $t1, 4
	
	# decrement number of pitches to play
	subu $t2, $t2, 1
	
	j loop

rest:
	li $v0, 32	# syscall 32 = sleep
	move $a0, $a1	# $a0 denotes how many milliseconds to sleep for
	syscall
	
	j cont # continue in the loop

end:
	# normal termination
	li $v0, 10
	syscall
	
