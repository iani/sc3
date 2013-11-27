// you need a plotter, check this -> http://aquaterm.sourceforge.net
// empirical data for demostrating the quasi-palindroms
// based on examples by Fredrik Ollofsson @ http://sccode.org/1-4TW
//1.
(
g= GNUPlot.new;
g.setXrange(-200,78200);
g.setYrange(-0.2,3.8);
// Latch.ar(Stepper.ar(vclk), clk)
{ Latch.ar(Stepper.ar(LFPulse.ar(377*Line.kr(12,6,1.5)), 0, 0, 3, step:1), LFPulse.ar(200)) }.loadToFloatArray(1.75, s, {|arr| g.plot2d(arr.clump(1), "y: degree", 'xclk = Line * constant')});
)
//same example in sc3
{Latch.ar(Stepper.ar(LFPulse.ar(377*Line.kr(12,6,1.5)), 0, 0, 3, step:1), LFPulse.ar(200))}.plot(1,minval:0,maxval:7)
//2.
(
g= GNUPlot.new;
g.setXrange(-200,22200);
g.setYrange(-0.2,7.8);
{ Latch.ar(Stepper.ar(LFPulse.ar(290*Line.kr(12,6,1.5)),step:1), LFPulse.ar(8*100)) }.loadToFloatArray(1.5, s, {|arr| g.plot2d(arr.clump(1), "y: degree", 'xclk = Line * LFTri * constant')});
)
//same example in sc3
{Latch.ar(Stepper.ar(LFPulse.ar(290*Line.kr(12,6,1.5)),step:1), LFPulse.ar(8*100))}.plot(1,minval:0,maxval:8)
//3.
(
g= GNUPlot.new;
g.setXrange(-200,22200);
g.setYrange(-0.2,7.8);
// clk:xclk = (1:1.9) * 3
{ Latch.ar(Stepper.ar(LFPulse.ar(290*LFTri.ar(2,add:1)*Line.kr(48,8,1.5)),step:1), LFPulse.ar(8*100)) }.loadToFloatArray(1.5, s, {|arr| g.plot2d(arr.clump(1), "y: degree", 'xclk = Line * LFTri * constant')});
)

