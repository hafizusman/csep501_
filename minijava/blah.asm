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
;  Line: 3
; Line 3
push	4
call	_mjmalloc
add	esp, 4
lea	edx, B$$
mov	[eax], edx
mov	ecx, eax
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
;  Line: 9
push	ebp
mov	ebp, esp
sub	esp, 4
;  Line: 12
mov	eax, 3
mov	[ebp -4], eax
;  Line: 14
L0:
mov	eax, [ebp -4]
push	eax
mov	eax, 11
mov	edx, eax
pop	eax
cmp	eax, edx
jge	L1
;  Line: 15
mov	eax, [ebp -4]
push	eax
mov	eax, 6
mov	edx, eax
pop	eax
cmp	eax, edx
jge	L2
mov	eax, [ebp -4]
push	eax
mov	eax, 5
mov	edx, eax
pop	eax
cmp	eax, edx
jge	L2
jge	L2
;  Line: 16
mov	eax, 123
push	eax
call	_put
add	esp,4
jmp	L3
L2:
;  Line: 19
mov	eax, 456
push	eax
call	_put
add	esp,4
L3:
;  Line: 21
mov	eax, [ebp -4]
push	eax
mov	eax, 1
mov	edx, eax
pop	eax
add	eax, edx
mov	[ebp -4], eax
jmp	L0
L1:
mov	eax, [ebp -4]
mov	esp, ebp
pop	ebp
ret
_TEXT	ENDS
END 
