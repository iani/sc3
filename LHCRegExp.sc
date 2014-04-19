// defining regular expressions for LHC.sc
/*
check out
http://www.regular-expressions.info/quickstart.html
*/
/*
EXAMPLE
(
var word = "BCBCBCCCCCDDCDDDCCCCCCCDCBDCBDCCCDDCCDDCDDBCDDBDDBDBDDCDBDBDCBCDBDCD";
var pointer = 0;
var currStr = "";
var tmp = 0;
 
word.do{ | i, k |
	if (word[k].asString == "D") {
		pointer = k;
		currStr = word.copyRange(tmp, pointer);
		//currStr.postln;
		LHCRegExp2(currStr);
		tmp = pointer + 1;
	};
}
)
*/
LHCRegExp {
 
	var regExp;
 
	*new { | str |
		^super.new.init(str);
	}
 
	init { | str |
		this.initRegexp;
		this.detectRegExp(str);
	}
 
	initRegexp {
		regExp = "^D|^B+D|^C+D|^(B+C+)+D|^(C+B+)+D|^(B+C+)+B+D|^(C+B+)+C+D";
 
	}
 
	detectRegExp { | str |
		if( regExp.matchRegexp(str, 0, str.size) == true ) {
			str.post; "\t (accepted)".postln;
		}
	}
 
}
testLHCRegExp.scdSuperCollider
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
// run a session on LHC
// then run the second snippet for recognise every word that ends with a ``D''
// d collects the produced symbols in an array
// ``A'' is for the empty string (\epsilon)
(
d = [];
a = LHC((
	symbol: { | self, symbol |
		self.soundOn.postln;
		if (self.soundOn) {
			(degree: (A: 11, B: 12, C: 13, D: 14)[symbol]).play;
			d = d ++ (A: "", B: "B", C: "C", D: "D")[symbol];
		}
	},
	counter: { | self, counter |
		if (counter > 0) {
			(degree: counter).play;
			self.soundOn = true;
		}{
			self.soundOn = false;
		}
	}
)).start;
)
// a.waitTime_(0.1)
// d.size
////////
// recognise every word that ends with a ``D''
(
var word = d.join;
var pointer = 0;
var currStr = "";
var tmp = 0;
 
word.do{ | i, k |
	if (word[k].asString == "D") {
		pointer = k;
		currStr = word.copyRange(tmp, pointer);
		currStr.postln;
		LHCRegExp(currStr);
		tmp = pointer + 1;
	};
}
)
