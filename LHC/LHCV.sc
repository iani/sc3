/*
// synth definition already written in SynthDef.synthDefDir
(
SynthDef(\LHCV, { | out=0, clk=1, vclk=2, id=0, step=0, resetVal=0|
	var clock, varclock, latch;
	clock = LFPulse.kr(clk);
	varclock = LFPulse.kr(vclk);
	latch = Latch.kr(Stepper.kr(varclock, step: step, resetval: resetVal), clock);
	Out.kr(out, SendTrig.kr(Changed.kr(latch), id, latch))
}).writeOnce;
)

*/

/*
TODO:
- There is a problem in the initial output of the counter? (must be zero)
- The secondary GUI (Slider) has a problem with it's flashDisplay and the initial position of the slider (though works OK).
- There are 2 dependants:
	* see var <>updater
	* I suppose it is feasible to have only one update method ..
- I have to fix the getter, setter vars
- ... and embed the slider to the main window !!
*/

/*

a = LHCV().start // see LHC example for musical example
a.counterOut // returns the current value of the Counter
a.vclk // returns the current value of the Var Clk rate (in Hz)
a.counterIn //  returns the current input
// set main clk (latched)
a.setClk_(value)
*/

LHCV : LHC {

	var <>clk=1, <>vclk=1;
	var <>synth;
	var <>counterIn=0, <>counterOut=0;
	var <>input=0;
	var osc;
	var <>binaryString;
	// the variables for the slider of the Var Clk rate
	var <>model, <>setValueFunction, <>bounds, <>windowVClk, <>numberboxVClk, <>sliderVClk, <>updater;
	var <posedgeDisplayVClk, <routineFlash;

	*new { | player |
		^super.new.init(player);
	}

	init { | player |
		this.makeFsmDecoder;
		this.addPlayer(player);
	}

	start {
		this.ugenconstr;
		this.oscrespond;
		this.makeWindow;
		this.makeRoutine;
		this.makeSlider;
	}

	ugenconstr { | clock=1, vclock=1, step=1, resetVal=0 |
		Server.default.waitForBoot {
			synth = Synth(\LHCV, [clk: clock, vclk: vclock, step: step, resetVal: resetVal]);
		}
	}

	oscrespond {
//		var n = NetAddr("127.0.0.1", NetAddr.langPort);
		osc = OSCFunc.newMatching({ arg msg, time;
			[time, msg].postln;
			counterOut = msg[3];
			counterOut.asInteger.asBinaryString(3).postln;
		},'/tr', Server.default.addr);
	}

	setInput_ { | value |
		input = value;
		synth.set(\step, input);
	}

	setClk_ { | value |
		clk = value;
		synth.set(\clk, clk);
	}

	setVClk_ { | value |
		vclk = value;
		synth.set(\vclk, vclk);
	}

	makeRoutine {
		if (routine.isNil) {
			routine = {
				loop {
					this.flashPositiveEdgeDisplay;
					counterIn = ([bit0.value, bit1.value, bit2.value] * [1, 2, 4]).sum;
					if (resetbutton.value > 0) {
						counterOut = 0;
						fsmDecoder.changed(\reset);
						"fsmDecoder should now broadcast change RESET".postln;
					}{
						// change step in Stepper
						this.setInput_(counterIn);
						fsmDecoder.changed(\counter, counterOut);
					};
					counterDisplay.value = counterOut;
					this.calculateFSMstate(counterOut);
					// clk is reciprocal because it's value is in Hz
					clk.reciprocal.wait;
				}
			}.fork(AppClock);
		}
	}

	flashPositiveEdgeDisplay {
		{
			positiveEdgeDisplay.value = 1;
			(clk / 3).wait;
			positiveEdgeDisplay.value = 0;
		}.fork(AppClock);
	}

	calculateFSMstate { | counter |
		// current state + input = next state + output
		{
			counter.asInteger.asBinaryString(3) do: { | digit |
				fsmDecoder input: (digit == $1).binaryValue;
				// clk is reciprocal because it's value is in Hz (see SynthDef(\LHCV,...)
				(clk.reciprocal / 3).wait;
			};
		}.fork(AppClock);
	}

	makeSlider {
		if (windowVClk.isNil) { this.prMakeSlider } { windowVClk.front };
	}

	prMakeSlider {
		this.flashDisplayVClk;
		//model
		model = (myValue: this.vclk);

		setValueFunction = {|value|
					model [\myValue] = value;
					model.changed(\value, value);
		};

		//bounds
		bounds = ControlSpec(0.01, 50, \linear, 0.01); // min, max, mapping, step
		//view
		windowVClk = Window("slider for LHCV", Rect(600,200, 280, 70));
		posedgeDisplayVClk = Button(windowVClk, Rect(10, 20, 18, 18))
					.states_([[" ", Color.black, Color.black], [" ", Color.yellow, Color.yellow]]);
		numberboxVClk = NumberBox(windowVClk, Rect(30, 20, 40, 20))
					.value_(model[\myValue])
					.action_({|view| setValueFunction.value(bounds.map(view.value)) });
		sliderVClk = Slider(windowVClk, Rect(73, 20, 200, 20))
					.value_(model[\myValue])
					.action_({|view| setValueFunction.value(bounds.map(view.value)) });

		windowVClk.front;
		windowVClk.onClose_({model.removeDependant(updater)});

		//updater
		updater = {|theChanger, what, val|
					{
					if(what == \value, {
						numberboxVClk.value_(val);
						sliderVClk.value_(bounds.unmap(val));
						//
						"VClk = ".post; val.value.postln;
						this.setVClk_(val.value);
				});
					}.defer;
				};
		model.addDependant(updater);

	}

	// the flasher for the Variable Clock rate
	// you can not run 2 different routines into the same Class?
	flashDisplayVClk {
		if ((routineFlash.isNil)&&(vclk.notNil)) {
			routineFlash = {
				loop {
					posedgeDisplayVClk.value = 1;
					vclk.reciprocal.wait;
					posedgeDisplayVClk.value = 0;
				}
			}.fork(AppClock);
		}
	}

	update { | who, what, value |
//		postf("% updated: % to: %\n", who, what, value);
		switch (what,
			\symbol, { encoderDisplay.string = value.asString },
			\state, { }
		);
	}

}