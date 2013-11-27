/*
- Using convolution in a signal it is possible to detect symmetric values.
- Though using real-time FFT doesn't work as expected.
  1. The plot should have twice the size of the signal.
  2. The plot should be - kati san synexh synarthsh
- Using Matlab works as expected.
- Maybe we should follow statistical analysis.

- Instead of all the above, the following code detects some palindromic structures
*/

/// wait 9 sec to plot
(
{
	a = Latch.ar(Stepper.ar(LFPulse.ar(Line.kr(99,1,9,1,0,2))),LFPulse.ar(9));
	[ a, Convolution.ar(a,a) ]

}.plot(9,minval:0,maxval:7)
)