; Listing generated by MuhammadUsman Non-optimizing Compiler Version <>

TITLE	blah.asm

	.686P 
	.XMM  
	include listing.inc 
	.model	flat 

INCLUDELIB LIBCMT 
INCLUDELIB OLDNAMES 

PUBLIC	_asm_main 
EXTRN	_put:PROC 
EXTRN  _get:PROC 
EXTRN  _mjmalloc:PROC 

_DATA	SEGMENT
BadContravariantParams$$:
	DD 0	;  no base class
	DD _asm_main	;  BadContravariantParams::main
B$$:
	DD 0	;  no base class
	DD B$my	;  B::my

_DATA	ENDS

_TEXT	SEGMENT 

_asm_main PROC	; main
; Line 3
push	ebp
mov	ebp, esp
; Line 3
push	12
call	_mjmalloc
add	esp, 4
lea	edx, B$$
mov	[eax], edx
mov	ecx, eax
<<<<<<< HEAD
mov	eax, 432
=======
mov	eax, 4
push	eax
mov	eax, 4
mov	edx, eax
pop	eax
add	eax, edx
push	eax
push	eax
mov	eax, 2
>>>>>>> parent of 4a18511... can read/write fields from our own class
push	eax
push	ecx
; Line 3
mov	eax, [ecx]
call	dword ptr [eax+4]
push	eax
call	_put
add	esp,4
mov	esp, ebp
pop	ebp
ret
_asm_main ENDP	; main
_TEXT	ENDS
_TEXT	SEGMENT
B$my:
<<<<<<< HEAD
;  Line: 11
push	ebp
mov	ebp, esp
sub	esp, 4
mov	eax, [ebp +12]
mov	esp, ebp
pop	ebp
ret
_TEXT	ENDS
END 
=======
;  Line: 9
push	ebp
mov	ebp, esp
sub	esp, 16
;  Line: 16
mov	eax, [ebp +12]
push	eax
mov	eax, 3
push	eax
mov	eax, [ebp +20]
mov	edx, eax
pop	eax
imul	eax, edx
mov	edx, eax
pop	eax
add	eax, edx
mov	[ebp -4], eax
;  Line: 17
mov	eax, [ebp +20]
mov	[ebp -8], eax
;  Line: 18
mov	eax, [ebp +16]
mov	[ebp -16], eax
;  Line: 19
mov	eax, 10
mov	[ebp -12], eax
>>>>>>> parent of 4a18511... can read/write fields from our own class
