//audible example - tweet like

play{p=Impulse;SendTrig.ar(Changed.ar(a=Latch.ar(Stepper.ar(p.ar(Line.kr(99,1,29,1,0,2))),p.ar(8))),0,a)};OSCFunc({|m|(degree:m[3],).play},'/tr')
