// Skoarpuscles are the closest thing we have to "types".
//
// They represent value types, as well as most things that
// can be spoken of as things, like a statement, boolean expression, etc.
//
Skoarpuscle {

    var <>val;
	var <>impressionable;
	var <>county;

    *new { | v | ^super.new.init(v); }
    init { 
		| v | 
		val = v;
		impressionable = true;
	}

    on_enter { | m, nav | }

    // override and implement .asNoat;
    isNoatworthy { ^false; }
    asNoat {SkoarError("asNoat called on " ++ this.class.asString ++ ", which is not noatworthy.").throw;}

	// override and implement .asCount;
    isCounty { ^false; }
    asCount {SkoarError("asCount called on " ++ this.class.asString ++ ", which is not county.").throw;}

    flatten { | m | ^val; }

    asString {
        var s = super.asString ++ ": " ++ val.asString;
        ^s;
    }

    skoar_msg {
        | msg, minstrel |
        var o = msg.get_msg_arr(minstrel);
        var ret = val.performMsg(o);

        ^Skoarpuscle.wrap(ret);
    }

    *wrap {
        | x |
        case {x.isNil} {
            //"wrapping: =^.^=".postln;
            ^SkoarpuscleCat();

		} {x == false} {
            //"wrapping: False".postln;
            ^SkoarpuscleFalse();

        } {x == true} {
            //"wrapping: True".postln;
            ^SkoarpuscleTrue();

        } {x.isKindOf(Skoarpuscle)} {
            //"already wrapped".postln;
            ^x;

        } {x.isKindOf(Skoarpion)} {
            //"wrapping: skoarpion".postln;
            ^SkoarpuscleSkoarpion(x);

        } {x.isKindOf(Integer)} {
            //("wrapping: int: " ++ x.asString).postln;
            ^SkoarpuscleInt(x);

        } {x.isKindOf(Number)} {
            //"wrapping: float".postln;
            ^SkoarpuscleFloat(x);

        } {x.isKindOf(String)} {
            //"wrapping: str".postln;
            ^SkoarpuscleString(x);

        } {x.isKindOf(Symbol)} {
            //"wrapping: symbol".postln;
            ^SkoarpuscleSymbol(x);

		} {x.isKindOf(UGen)} {
			^SkoarpuscleUGen(x);
			
        } {x.isKindOf(Array)} {
            var a = Array.newClear(x.size);
            var i = -1;
            //"wrapping: array".postln;
            x.do {
                | el |
                i = i + 1;
                a[i] = Skoarpuscle.wrap(el);
            };

            ^SkoarpuscleList(a);
        } {
            //"wrapping: unknown: ".post; x.dump;
            ^SkoarpuscleUnknown(x);
        };

    }
}

SkoarpuscleUnknown : Skoarpuscle {

}

// Cats show up in unexpected places.
SkoarpuscleCat : Skoarpuscle {

	init {
		val = "=^.^=";
		impressionable = true;
	}

    asString {^"=^.^="}

    skoar_msg {}
    flatten {^nil}

	on_enter {
        | m, nav |
		//"=^.^=".postln;
        m.fairy.impress(this);
    }
}

SkoarpuscleFalse : Skoarpuscle {
	
	init {
		val = false;
	}

	asString {^"false"}

    skoar_msg {}
    flatten {^false}

	on_enter {
        | m, nav |
        m.fairy.impress(this);
    }
}

SkoarpuscleTrue : Skoarpuscle {

	init {
		val = true;
	}

	asString {^"true"}

    skoar_msg {}
    flatten {^true}
	
	on_enter {
        | m, nav |
        m.fairy.impress(this);
    }
}

SkoarpuscleInt : Skoarpuscle {

    isNoatworthy { ^true; }

    asNoat {
        ^SkoarNoat_Degree(val.asInteger);
    }

    isCounty { ^true; }

    asCount {
        ^val.asInteger;
    }

    flatten {
        | m |
        ^val.asInteger;
    }

	on_enter {
        | m, nav |
        m.fairy.impress(this);
    }

}

SkoarpuscleFloat : Skoarpuscle {

    isNoatworthy { ^true; }

    asNoat {
        ^SkoarNoat_Degree(val.asFloat);
    }

    flatten {
        | m |
        ^val.asFloat;
    }

	on_enter {
        | m, nav |
        m.fairy.impress(this);
    }

}

SkoarpuscleFreq : Skoarpuscle {

    init {
        | lexeme |
		if (lexeme.isKindOf(String)) {
			val = lexeme[0..lexeme.size-3].asFloat;
		} {
			val = lexeme.asFloat;
		};
	}

    isNoatworthy { ^true; }

    asNoat {
        ^SkoarNoat_Freq(val);
    }

    on_enter {
        | m, nav |
        m.fairy.impress(this);
    }
}


SkoarpuscleString : Skoarpuscle {

    on_enter {
        | m, nav |
        m.fairy.impress(this);
    }
}

SkoarpuscleSymbolName : Skoarpuscle {
}

SkoarpuscleSymbol : Skoarpuscle {

	init {
		| v |

		if (v.isKindOf(Symbol) == false) {
			v = v.asSymbol;
		};
		val = v;
	}

    on_enter {
        | m, nav |
        m.fairy.impress(this);
    }

    skoar_msg {
        | msg, minstrel |
        var o = msg.get_msg_arr(minstrel);
        var ret = val.asClass.performMsg(o);

        ^Skoarpuscle.wrap(ret);
    }
}

SkoarpuscleSymbolColon : Skoarpuscle {

	init {
		| lex |
		// lexeme was matched by: [a-zA-Z0-9_][a-zA-Z0-9_]*[ \t]*:(?![:|}])
        var regex = "[a-zA-Z0-9_][a-zA-Z0-9_]*";
        val = lex.findRegexpAt(regex, 0)[0].asSymbol;
	}

}


SkoarpuscleDeref : Skoarpuscle {

    var msg_arr;
    var <args;

    *new {
        | v, a |
        ^super.new.init(v, a);
    }

    init {
        | v, a |
        val = v;
        args = a;
    }

    lookup {
        | m |
        ^m.koar[val];
    }

    flatten {
        | m |
        ^this.lookup(m);
    }

    on_enter {
        | m, nav |
		if (args.isNil) {
			this.do_deref(m, nav);
		} {
			args.on_enter(m, nav);
		};
    }

	on_exit {
		| m, nav |
		this.do_deref(m, nav);
	}

	do_deref {
		| m, nav |
		var x = this.lookup(m);

		//"deref:on_enter: SYMBOL LOOKEDUP : ".post; val.post; " ".post; x.postln;
		x = Skoarpuscle.wrap(x);

		if (x.isKindOf(SkoarpuscleSkoarpion)) {
			var impression = m.fairy.impression;
			//"passing args: ".post; impression.class.asString.postln;													
			m.koar.do_skoarpion(x.val, m, nav, msg_arr, impression);
		} {
			m.fairy.impress(x);
		};
	}

    skoar_msg {
        | msg, minstrel |
        var ret = val;
        var x = this.lookup(minstrel);

        //"deref:skoar_msg: SYMBOL LOOKEDUP : ".post; val.post; " ".post; x.postln;
        msg_arr = msg.get_msg_arr(minstrel);

        if (x.isKindOf(SkoarpuscleSkoarpion)) {
            ^this;
        };

        // we don't recognise that name, did they mean a SuperCollider class?
        if (x.isNil) {
            x = val.asClass;
        };

        if (x.notNil) {
            if (x.isKindOf(SkoarpuscleString)) {
                x = x.val;
            };
            ret = x.performMsg(msg_arr);
        };

        ^Skoarpuscle.wrap(ret);
    }

}

SkoarpuscleMathOp : Skoarpuscle {
    var f;

    init {
        | toke |
        val = toke.lexeme;

        f = switch (val)
            {"+"}  {{
                | minstrel, a, b |
                Skoar.ops.add(minstrel, a, b);
            }}

            {"*"}  {{
                | minstrel, a, b |
                Skoar.ops.multiply(minstrel, a, b);
            }}

            {"-"}  {{
                | minstrel, a, b |
                Skoar.ops.sub(minstrel, a, b);
            }};
    }

    calculate {
        | m, nav, left, right |

        // the result is impressed by the operation
        f.(m, left, right);
    }

}

SkoarpuscleBooleanOp : Skoarpuscle {

    var f;
	var <noad;

    *new {
        | n, toke |
        ^super.new.init_two(n, toke);
    }

    init_two {
        | n, toke |
        val = toke.lexeme;
		noad = n;

        // ==|!=|<=|>=|in|nin|and|or|xor
        f = switch (val)
            {"=="}  {{
                | a, b |
                a == b
            }}
            {"!="}  {{
                | a, b |
                a != b
            }}
            {"<="}  {{
                | a, b |
                a <= b
            }}
            {"<"}  {{
                | a, b |
                a < b
            }}
            {">="}  {{
                | a, b |
                a >= b
            }}
            {">"}  {{
                | a, b |
                a > b
            }}
            {"and"} {{
                | a, b |
                a and: b
            }}
            {"or"}  {{
                | a, b |
                a or: b
            }}
            {"xor"} {{
                | a, b |
                a xor: b
            }};

    }

    compare {
        | m, nav, a, b |
     
        if (a.isKindOf(Skoarpuscle)) {
            //debug(a);
            a = a.flatten(m);
        };

        if (b.isKindOf(Skoarpuscle)) {
            //debug(b);
            b = b.flatten(m);
        };

        //("===========   {? " ++ a.asString ++ " " ++ val ++ " " ++ b.asString ++ " ?}").postln;

		//a !? b !? {
		^f.(a, b)
		//};
		
        //^false
    }

	on_enter {
		| m, nav |
		m.fairy.cast_arcane_magic;
		m.fairy.compare_impress(m);
	}

}

SkoarpuscleBoolean : Skoarpuscle {

    var <op;

    init {
        | noad |
        op = noad.children[0].next_skoarpuscle;
    }

	on_enter {
		| m, nav |
		m.fairy.push_compare;
	}

    evaluate {
        | m, nav, a, b |
		//("slrp " ++ a.asString ++ " imp: " ++ b.asString).postln;
        ^op.compare(m, nav, a, b);
    }

}


SkoarpuscleConditional : Skoarpuscle {

    var ifs;

    *new {
        | skoar, noad |
        ^super.new.init_two(skoar, noad);
    }

    init_two {
        | skoar, noad |
		var i = 0;
        ifs = [];

        noad.collect(\cond_if).do {
            | x |
            var condition = Skoarpion.new_from_subtree(skoar, x.children[0]);
            var if_body;
            var else_body;

            if_body = Skoarpion.new_from_subtree(skoar, x.children[2]);

            else_body = x.children[4];
            if (else_body.notNil) {
                else_body = Skoarpion.new_from_subtree(skoar, else_body);
            };

            ifs = ifs.add([condition, if_body, else_body]);
        };
		noad.children = #[];

    }

    on_enter {
        | m, nav |

        ifs.do {
            | x |
            var condition = x[0];
            var if_body = x[1];
            var else_body = x[2];
			var impression;

			m.koar.do_skoarpion(condition,
                m, nav, [\inline], nil
            );

			impression = m.fairy.boolean_impression;
            m.koar.do_skoarpion(
                if (impression.isKindOf(SkoarpuscleFalse) or: impression.isKindOf(SkoarpuscleCat)) {
						else_body
					} {
						if_body
					},
                m, nav, [\inline], nil
            );
        };
    }

}

SkoarpuscleSkoarpion : Skoarpuscle {

    var msg_arr;

    skoar_msg {
        | msg, minstrel |
        msg_arr = msg.get_msg_arr(minstrel);
        ^this;
    }

    on_enter {
        | m, nav |
        if (val.name.notNil) {
            m.koar[val.name] = this;
        };

        if (msg_arr.notNil) {
			//m.fairy.impression.postln;
			//msg_arr.postln;

            m.koar.do_skoarpion(val, m, nav, msg_arr, m.fairy.impression);
        };

		if (val.name.isNil) {
			m.fairy.impress(this);
		};
    }

}

SkoarpuscleTimes : Skoarpuscle {

	on_enter {
        | m, nav |
		var desired_times = m.fairy.cast_arcane_magic;
		
		if (desired_times.isCounty) {
			var times_seen = m.fairy.how_many_times_have_you_seen(this);
			desired_times = desired_times.asCount;

			//("desired_times: " ++ desired_times.asString ++ "\n times seen: " ++ times_seen.asString).postln;
			m.fairy.impress( ( times_seen % desired_times != 0) );
		};
		 
    }

}

SkoarpuscleLoop : Skoarpuscle {

    var <condition;
    var <body;
    var <each;

    *new {
        | skoar, noad |
        ^super.new.init_two(skoar, noad);
    }

    init_two {
        | skoar, noad |

        noad.collect(\loop_condition).do {
            | x |
			if (x.children.size > 0) {
				condition = Skoarpion.new_from_subtree(skoar, x);
			};
        };

        noad.collect(\loop_body).do {
            | x |
            body = Skoarpion.new_from_subtree(skoar, x);
        };

        each = nil;
    }

    on_enter {
        | m, nav |
        
		m.fairy.push_i;

        block {
            | break |
            while {true} {
                var f = {
                    | element |

                    // this is how we foreach
                    if (element.isKindOf(Skoarpuscle)) {
                        m.fairy.impress(element);
                    };

                    m.koar.do_skoarpion(body, m, nav, [\inline], m.fairy.impression);
                    m.fairy.incr_i;

					if (condition.notNil) {
						var x;

						m.koar.do_skoarpion(condition, m, nav, [\inline]);
                        x = m.fairy.boolean_impression;
						if (x.isKindOf(SkoarpuscleFalse) or: x.isKindOf(SkoarpuscleCat)) {
							break.();
						};
                    };
                };

                if (each.isNil) {
                    f.(nil);
                } {
                    //debug("each: " ++ each.asString);
                    each.val.do {
                        | element |
                        //debug("each:el: " ++ element.asString);
                        f.(element);
                    };
                };
				
				//"zorp: condition: ".post; condition.postln;
                if (condition.isNil) {
                    break.();
                };
            };
        };

		m.fairy.pop_i;
    }

    // when we send a loop as a message, the receiver
    // goes into _each_ and _this_ becomes the new receiver.
    foreach {
        | listy |
        each = listy;
        ^this;
    }
}


SkoarpuscleLoopMsg : Skoarpuscle {
}

SkoarpuscleCuts : Skoarpuscle {

    var skoarpion;

    *new {
        | skoar, noad |
        ^super.new.init_two(skoar, noad);
    }

    init_two {
        | skoar, noad |

        skoarpion = Skoarpion.new_from_subtree(skoar, noad);
    }

    on_enter {
        | m, nav |
        
		m.fairy.push_i;

		m.koar.do_skoarpion(skoarpion, m, nav, [\cuts], m.fairy.impression);

		m.fairy.pop_i;
    }

}


SkoarpuscleCutsMsg : Skoarpuscle {
}

SkoarpuscleExprEnd : Skoarpuscle {

    on_enter {
        | m, nav |
        m.fairy.cast_arcane_magic;
    }
}

SkoarpuscleListSep : Skoarpuscle {

    on_enter {
        | m, nav |
        m.fairy.next_listy;
    }
}

SkoarpuscleListEnd : Skoarpuscle {

    on_enter {
        | m, nav |
		//"sna".postln;
        m.fairy.next_listy;
        m.fairy.pop;
		//"snarps".postln;
    }

	
}

SkoarpuscleList : Skoarpuscle {

    init {
        | x |
		val = if (x.isNil) {
			[]
		} {
			x
		};
		//"new list: ".post; val.postln;
    }

	size {
		^val.size;
	}
	
	flatten { | m | 
		var arr = [];
		val.do {
			| x |
			var y = x.flatten(m);
			if (y.isKindOf(SkoarpusclePair)) {
				y = y.flatten(m);
			};
			//("Adding " ++ y).postln;
			arr = arr.add(y);
		};
		^arr; 
	}

    on_enter {
        | m, nav |
        m.fairy.push;
    }

    isNoatworthy {

        val.do {
            | x |
            if (x.isNoatworthy == false) {
                ^false;
            };
        };

        ^true;
    }

    asNoat {

        var n = val.size;
        var noats = Array.newClear(n);
        var i = -1;
		var theseAreNoats = true;
		var theseAreFreq = false;
		
        val.do {
            | x |
            i = i + 1;

			if (x.isKindOf(SkoarpuscleFreq)) {
				theseAreNoats = false;
				theseAreFreq = true;
			};

			if (x.isKindOf(SkoarpuscleInt)) {
				theseAreNoats = false;
			};

			if (x.isKindOf(SkoarpuscleFloat)) {
				theseAreNoats = false;
			};
			noats[i] = x.asNoat;
        };

		if (theseAreNoats == true) {
			^SkoarNoat_NoteList(noats);
		};

		if (theseAreFreq == true) {
			^SkoarNoat_FreqList(noats);
		};

		^SkoarNoat_DegreeList(noats);
	}


	skoar_msg {
        | msg, minstrel |
        var o = msg.get_msg_arr(minstrel);
        var name = msg.val;
        var ret;

		// todo teach the fairy to next and last
        case {name == \next} {
            ret = val.performMsg(o);
        } {name == \last} {
            ret = val.performMsg(o);
        } {name == \choose} {
            ret = val.choose();
        } {
            ret = val.performMsg(o);
        };

        ^Skoarpuscle.wrap(ret);
    }
    

}

SkoarpuscleArgs : SkoarpuscleList {

}

// {! f<a,b,c> !! '[\a,\b,\c] is the ArgsSpec' !}
SkoarpuscleArgSpec : Skoarpuscle {

    init {
        | toke |
        var matches = toke.lexeme.findRegexp("[a-zA-Z_]*",1);

		if (matches.notNil) {
			matches.do {
				| x |
				var m = x[1];
				if (m != "") {
					val = val.add(SkoarpuscleSymbolName(m.asSymbol));
				};
			};
		};
    }
}

SkoarpuscleMsg : Skoarpuscle {

    var <>args;
	var <>dest;

    *new {
        | v, a |
        ^super.new.init(nil).init(v, a);
    }

    init {
        | v, a |
        val = v;
        args = a;
    }

	asString {
		^(super.asString ++ " -> " ++ dest.asString);
	}

    on_enter {
        | m, nav |
		var result;

		if (args.notNil) {
			args = m.fairy.impression;
			//"msg::impression (args): ".post; args.postln;
		};
		//args = args.flatten(m);
		//"msg::flattened args   : ".post; args.postln;
		
		if (dest.isKindOf(SkoarpuscleList)) {
			result = m.fairy.impression;
			result = result.skoar_msg(this, m);
			//"msg::impressing (result): ".post; result.postln;
		} {
			result = dest.skoar_msg(this, m);
			//"msg::impressing (from dest): ".post; result.postln;
		};
		m.fairy.impress(result);
    }

    get_msg_arr {
        | m |
		var x;
		var i;

		if (args.isNil) {
			^[val];
		};

		//("Args exist, x will be " ++ (args.size + 1).asString).postln; 
		x = Array.newClear(args.size + 1);
		i = 0;

		//"args: ".post; args.postln;

		x[0] = val;
		args.flatten(m).do {
			| y |
			//"y: ".post; y.postln;
			i = i + 1;
			x[i] = y;
		};
		^x;
		
    }
}

SkoarpuscleMsgName : Skoarpuscle {
	
}

SkoarpuscleMsgNameWithArgs : Skoarpuscle {
	on_enter {
		| m, nav |
		m.fairy.push;
	}
}

// -----------------------------
// musical keywords skoarpuscles
// -----------------------------

SkoarpuscleBars : Skoarpuscle {

    var <>noad; // is set in \markers skoarmantics
    var <pre_repeat;
    var <post_repeat;

    init {
        | toke |

        val = toke.lexeme;
        pre_repeat = val.beginsWith(":");
        post_repeat = val.endsWith(":");
    }

	asString {
		var s = ("SkoarpuscleBars: " ++ val ++ "noad:"++ noad.asString ++ 
		    " pre_repeat:" ++ pre_repeat ++ " post_repeat:" ++ post_repeat);
		^s;
	}

    on_enter {
        | m, nav |

		//"on_enter: ".post; this.asString.postln;
        
		// :|
        if (pre_repeat == true) {

			var burned = m.koar.state_at(\colons_burned);

			// asking counts as observing
			if (m.fairy.how_many_times_have_you_seen(this) < 2) {
				nav.(\nav_colon);
			};	

			if (burned.falseAt(noad)) {
                burned[noad] = true;
            };

        };

        // |:
        if (post_repeat == true) {
            m.koar.state_put(\colon_seen, noad);
        };
    }
}

SkoarpuscleFine : Skoarpuscle {

    on_enter {
        | m, nav |
        if (m.koar.state_at(\al_fine) == true) {
            //debug("fine");
            nav.(\nav_fine);
        };
    }
}

SkoarpuscleSegno : Skoarpuscle {

    var <noad;

    *new {
        | nod, toke |
        ^super.new.init_two(nod, toke);
    }

    init_two {
        | nod, toke |
        var s = toke.lexeme;
        var n = s.size;

        noad = nod;

        // ,segno`label`
        if (n > 8) {
            s[6..n-2].asSymbol;
        } {
            \segno
        };
        val = s[1..n].asSymbol;
    }

    on_enter {
        | m, nav |
        m.koar.state_put(\segno_seen, noad);
    }

}

SkoarpuscleGoto : Skoarpuscle {

    var nav_cmd;
    var al_fine;

    init {
        | noad |

        var toke = noad.children[0].next_toke;
        var al_x = noad.children[1];

        nav_cmd = case {toke.isKindOf(Toke_DaCapo)} {\nav_da_capo}
                       {toke.isKindOf(Toke_DalSegno)} {\nav_segno};

        al_fine = false;
        if (al_x.notNil) {
            if (al_x.next_toke.isKindOf(Toke_AlFine)) {
                al_fine = true;
            };
        };
    }

    on_enter {
        | m, nav |
        if (al_fine == true) {
            m.koar.state_put(\al_fine, true);
        };

        m.reset_colons;
        //"goto:".post; nav_cmd.postln;
        nav.(nav_cmd);
    }

}

SkoarpuscleVolta : Skoarpuscle {

    var <noad;

    *new {
        | nod, toke |
        ^super.new.init_two(nod, toke);
    }

    init_two {
        | nod, toke |
        val = toke.lexeme.strip("[.]").asInteger;
        noad = nod;
    }

    on_enter {
        | m, nav |
    }

}

SkoarpuscleMeter : Skoarpuscle {

    init {
        | toke |
        var a = toke.lexeme.split;
        val = [a[0].asInteger, a[1].asInteger];
    }
}

SkoarpuscleCarrots : Skoarpuscle {

    init {
        | toke |
        val = toke.lexeme.size;
    }
}

SkoarpuscleTuplet : Skoarpuscle {

    init {
        | toke |
        val = toke.lexeme.size;
    }
}

SkoarpuscleDynamic : Skoarpuscle {

    init {
        | toke |
        var s = toke.lexeme;

        val = switch (s)
            {"ppp"}			{1}
            {"pppiano"}		{1}
            {"pp"}			{2}
            {"ppiano"}		{2}
            {"p"}			{3}
            {"piano"}		{3}
            {"mp"}			{4}
            {"mpiano"}		{4}
			{"mezzopiano"}  {4}
            {"mf"}			{5}
            {"mforte"}		{5}
            {"mezzoforte"}  {5}
            {"forte"}		{6}
            {"ff"}			{7}
            {"fforte"}		{7}
            {"fff"}			{8}
            {"ffforte"}		{8};
    }

    amp {
        ^val/8;
    }

    on_enter {
        | m, nav |
        m.koar[\amp] = this.amp;
    }

}

SkoarpuscleOctaveShift : Skoarpuscle {

    init {
        | toke |
        var f = {
            var s = toke.lexeme;
            var n = s.size - 1;

            if (s.beginsWith("o")) {
                n =  n * -1;
            };
            n
        };

        val = case {toke.isKindOf(Toke_OctaveShift)} {f.()}
                   {toke.isKindOf(Toke_OttavaA)}       { 1}
                   {toke.isKindOf(Toke_OttavaB)}       {-1}
                   {toke.isKindOf(Toke_QuindicesimaA)} { 2}
                   {toke.isKindOf(Toke_QuindicesimaB)} {-2};
    }

    on_enter {
        | m, nav |
        var octave = m.koar[\octave] ?? 5;
        m.koar[\octave] = octave + val;
    }

}

SkoarpuscleVoice : Skoarpuscle {

    init {
        | toke |
        var s = toke.lexeme;
        var n = s.size - 1;
        val = s[1..n].asSymbol;
    }

}

SkoarpuscleRep : Skoarpuscle {

    init {
        | toke |
        val = toke.lexeme.size;
    }

}

SkoarpuscleHashLevel : SkoarpuscleFloat {

	init {
		| toke |
		var n = -2, i = 0;
		toke.lexeme.do {
			| x |
			if (x.notNil) {
				n = n + 1;
				if (x == $#) {
					i = i + 1;
				};
			}; 
		};

		val = if (n <= 0) {
			0.0
		} {
			i/n
		};
	}

}

SkoarpuscleUGen : Skoarpuscle {

	var klass;
	var <ugen;
	var <args;
	var <synthdef;
	var <name;
	var <>bound;
	var <func;
	var <rate;

	
	*new {
		| k, a, b, f |
		^super.new.init_copy(k, a, b, f);
	}

	*new_from_toke {
		| t |
		^super.new.init_toke(t);
	}
	
	
	init {
		| k |
		val = k;
		bound = IdentityDictionary.new;
		args = IdentityDictionary.new;
	}

	init_toke {
		| t |
		val = t.lexeme[1..].asSymbol;
		klass = val.asClass;
		bound = IdentityDictionary.new;
		args = IdentityDictionary.new;
		func = {
			| e |
			if (klass.respondsTo(\ar)) {
				klass.performWithEnvir(\ar, e)
			} {
				klass.performWithEnvir(\kr, e)
			}
		};
		
	}

	init_copy {
		| k, a, b, f |
		klass = k;
		val = k.asSymbol;
		bound = b;
		args = a;
		func = f;
	}

	add {
		| x |
		var f = if (x.isKindOf(SkoarpuscleUGen)) {
			{ | e | func.value(e) + x.func.value(e) }
		} {
			{ | e | func.value(e) + x.val(e) }
		};
			
		^SkoarpuscleUGen.new(klass, args, bound, f);
	}

	sub {
		| x |
		var f = if (x.isKindOf(SkoarpuscleUGen)) {
			{ | e | func.value(e) - x.func.value(e) }
		} {
			{ | e | func.value(e) - x.val(e) }
		};

		^SkoarpuscleUGen.new(klass, args, bound, f);
	}

	mul {
		| x |
		var f = if (x.isKindOf(SkoarpuscleUGen)) {
			{ | e | func.value(e) * x.func.value(e) }
		} {
			{ | e | func.value(e) * x.val(e) }
		};
		^SkoarpuscleUGen.new(klass, args, bound, f);
	}
	
	flatten {
		| m |
		^func;
	}

	on_enter {
        | m, nav |
		//"ON_ENTER".postln;
		m.fairy.impress(this);
	}
	
	
	skoar_msg {
		| msg, minstrel |
        var ret;
        
		var msg_selector = msg.val;
        args = msg.get_msg_arr(minstrel);
		//"AAAAA".postln;
		//args.postln;
		if (args.size == 1) {
			func = {
				| e |
				klass.performWithEnvir(msg_selector, e)
			};
		} {
			func = {
				| e |
				klass.performMsg(args)
			};
		};
		minstrel.fairy.impress(this);
	}
	
	compile_synthdef {
		| m, nav |
		synthdef = {
			| pan=0, freq=440, amp=0.2, att=0.01, rel=1, sustain=1, dec=0.1, gate=1 | 
			var e = (pan: pan, freq: freq, amp: amp, att: att, rel: rel, sustain: sustain, dec: dec, gate: gate);
			
			//func.value(e) * EnvGen.kr(Env.perc, doneAction:2);
			func.value(e) * EnvGen.kr(Env.linen(att, sustain*0.5, sustain*0.5), doneAction:2)!2;
		}.asSynthDef;

		synthdef.add;
		name = synthdef.asDefName;
		//(name ++ " defined").postln;
		//synthdef.dump;
	}

	
}

/*
out = 0, to be used in Out.ar(out, ...)
pan = 0, to be used in Out.ar(out, Pan2.ar(snd, pan))
freq = 440, always the main freq for pitched synths
amp = 0.2, always the main amplitude of the final sound
att = 0.01, attack time of the main amplitude envelope
rel = 1, release time of amplitude envelope
sus = 1, sustain amount (as in a typical ADSR)
dec = 0.1, decay time (as in typical ADSR)
gate = 1, if using envelopes like ADSR (not needed if using self terminating envs such as Env.perc)
*/

//(out: 0, pan: 0, freq: 440, amp: 0.2, att: 0.01, rel: 1, sustain: 1, dec: 0.1, gate: 1)

SkoarpusclePair : Skoarpuscle {

	var <key;
	
	*new {
        | k, v |
        ^super.new.init_two(k, v);
    }

    init_two {
		| k, v |
		key = k;
		val = v;
	}

	flatten {
		| m, nav |
		^val.flatten(m, nav);
	}

	assign {
		| m |
		Skoar.ops.assign(m, val, key);
	}
	
}
