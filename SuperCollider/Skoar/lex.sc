// ================================================================================
// lex.sc - Generated by Code_Sc_Lexer on 2015-01-19 13:09:50 for SuperCollider 3.7
// ================================================================================
// --------------
// SkoarException
// --------------
SkoarError : Exception {
    *new {
        | msg |

        ^super.new(msg);
    }

    *errorString {
        ^"SKOAR" ++ super.errorString;
    }

}

// --------------
// Abstract Token
// --------------
SkoarToke {
    var <lexeme;
    var size;
    classvar <regex = nil;

    *new {
        | s, n |

        ^super.new.init( s, n );
    }

    init {
        | s, n |

        lexeme = s;
        size = n;
    }

    // how many characters to burn from the buffer
    burn {
        ^size;
    }

    // override and return nil for no match, new toke otherwise
    *match {
        | buf, offs |

        SubclassResponsibilityError("What are you doing human?").throw;
    }

    // match requested toke
    *match_toke {
        | buf, offs, toke_class |

        var match;
        match = buf.findRegexpAt(toke_class.regex, offs);
        if (match.isNil) {
            ^nil;
        };

        ^toke_class.new(match[0], match[1]);
    }

}

// ---------------------
// Whitespace is special
// ---------------------
Toke_Whitespace : SkoarToke {
    classvar <regex = "[ \\t]*";

    *burn {
        | buf, offs |

        var match;
        match = buf.findRegexpAt(Toke_Whitespace.regex, offs);
        if (match != nil) {
            ^match[1];
        };

        ^0;
    }

}

// --------------
// EOF is special
// --------------
Toke_EOF : SkoarToke {
    *burn {
        | buf, offs |

        if (buf.size > offs) {
            SkoarError("Tried to burn EOF when there's more input.").throw;
        };

        ^0;
    }

    *match {
        | buf, offs |

        if (buf.size < offs) {
            SkoarError("Tried to burn EOF when there's more input.").throw;
        };

        if (buf.size == offs) {
            ^Toke_EOF.new();
        };

        ^nil;
    }

}

// --------------
// Everyday Tokes
// --------------
Toke_LoopE : SkoarToke {
    classvar <regex = ":[}]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_LoopE);
    }

}

Toke_CondIf : SkoarToke {
    classvar <regex = "[?][?](?![}])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_CondIf);
    }

}

Toke_AssOp : SkoarToke {
    classvar <regex = "=>|[+]>|->";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_AssOp);
    }

}

Toke_DalSegno : SkoarToke {
    classvar <regex = "D\\.S\\.|Dal Segno";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DalSegno);
    }

}

Toke_Fine : SkoarToke {
    classvar <regex = "fine";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Fine);
    }

}

Toke_LoopS : SkoarToke {
    classvar <regex = "[{]:[\\n]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_LoopS);
    }

}

Toke_AlCoda : SkoarToke {
    classvar <regex = "al(la)? coda";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_AlCoda);
    }

}

Toke_PedalUp : SkoarToke {
    classvar <regex = "[*](?!>)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_PedalUp);
    }

}

Toke_Quavers : SkoarToke {
    classvar <regex = "o+/\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Quavers);
    }

}

Toke_AlSegno : SkoarToke {
    classvar <regex = "al segno";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_AlSegno);
    }

}

Toke_Fairy : SkoarToke {
    classvar <regex = "[$]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Fairy);
    }

}

Toke_CondS : SkoarToke {
    classvar <regex = "[{][?][\\n]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_CondS);
    }

}

Toke_QuindicesimaB : SkoarToke {
    classvar <regex = "15mb|alla quindicesimb";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_QuindicesimaB);
    }

}

Toke_Int : SkoarToke {
    classvar <regex = "(-)?(0|[1-9][0-9]*)(?![mv][ab]|\\.[0-9]|/)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Int);
    }

}

Toke_MsgName : SkoarToke {
    classvar <regex = "[a-zA-Z_][a-zA-Z0-9_]*(?!<)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_MsgName);
    }

}

Toke_ListS : SkoarToke {
    classvar <regex = "<(?![=?])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_ListS);
    }

}

Toke_SkoarpionEnd : SkoarToke {
    classvar <regex = "![}]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_SkoarpionEnd);
    }

}

Toke_LWing : SkoarToke {
    classvar <regex = "\\^\\^[(]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_LWing);
    }

}

Toke_Loco : SkoarToke {
    classvar <regex = "loco";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Loco);
    }

}

Toke_Volta : SkoarToke {
    classvar <regex = "\\[\\d+\\.\\]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Volta);
    }

}

Toke_MsgOp : SkoarToke {
    classvar <regex = "\\.(?![)\\]])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_MsgOp);
    }

}

Toke_Eighths : SkoarToke {
    classvar <regex = "\\.?\\]+(?:__?)?\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Eighths);
    }

}

Toke_PedalDown : SkoarToke {
    classvar <regex = "Ped\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_PedalDown);
    }

}

Toke_Float : SkoarToke {
    classvar <regex = "(-)?(0|[1-9][0-9]*)\\.[0-9]+";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Float);
    }

}

Toke_SkoarpionSep : SkoarToke {
    classvar <regex = "!!";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_SkoarpionSep);
    }

}

Toke_DynPiano : SkoarToke {
    classvar <regex = "(mp|p+)(iano)?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DynPiano);
    }

}

Toke_Caesura : SkoarToke {
    classvar <regex = "//";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Caesura);
    }

}

Toke_Comment : SkoarToke {
    classvar <regex = "<[?](.|[\\n\\r\\f])*?[?]>";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Comment);
    }

}

Toke_ListE : SkoarToke {
    classvar <regex = ">";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_ListE);
    }

}

Toke_MathOp : SkoarToke {
    classvar <regex = "[+x\\-](?!>)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_MathOp);
    }

}

Toke_Nosey : SkoarToke {
    classvar <regex = ",";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Nosey);
    }

}

Toke_DynSFZ : SkoarToke {
    classvar <regex = "sfz";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DynSFZ);
    }

}

Toke_AlFine : SkoarToke {
    classvar <regex = "al fine";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_AlFine);
    }

}

Toke_Choard : SkoarToke {
    classvar <regex = "(D(?![a.])|[ABCEFG])([Mm0-9]|sus|dim)*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Choard);
    }

}

Toke_String : SkoarToke {
    classvar <regex = "'[^']*'";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_String);
    }

}

Toke_Quarters : SkoarToke {
    classvar <regex = "\\.?[)]+(?:__?)?\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Quarters);
    }

}

Toke_Portamento : SkoarToke {
    classvar <regex = "port\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Portamento);
    }

}

Toke_DynForte : SkoarToke {
    classvar <regex = "mf(orte)?|f+orte|ff+";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DynForte);
    }

}

Toke_OttavaB : SkoarToke {
    classvar <regex = "8vb|ottava (bassa|sotto)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_OttavaB);
    }

}

Toke_LoopSep : SkoarToke {
    classvar <regex = "::[\\n]*(?![|])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_LoopSep);
    }

}

Toke_Voice : SkoarToke {
    classvar <regex = "\\.(([a-zA-Z_][a-zA-Z0-9_]*)?|\\.+)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Voice);
    }

}

Toke_Segno : SkoarToke {
    classvar <regex = ",segno`(?:_[a-zA-Z_][a-zA-Z0-9_]*`)*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Segno);
    }

}

Toke_BooleanOp : SkoarToke {
    classvar <regex = "==|!=|<(?!=)|<=|>(?!=)|>=|and|or|xor";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_BooleanOp);
    }

}

Toke_CondE : SkoarToke {
    classvar <regex = "[?][}]";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_CondE);
    }

}

Toke_QuindicesimaA : SkoarToke {
    classvar <regex = "15ma|alla quindicesima";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_QuindicesimaA);
    }

}

Toke_Slash : SkoarToke {
    classvar <regex = "/(?![/0-9])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Slash);
    }

}

Toke_DynFP : SkoarToke {
    classvar <regex = "fp";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DynFP);
    }

}

Toke_Newline : SkoarToke {
    classvar <regex = "[\\n\\r\\f][\\n\\r\\f \\t]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Newline);
    }

}

Toke_Meter : SkoarToke {
    classvar <regex = "[1-9][0-9]*/[1-9][0-9]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Meter);
    }

}

Toke_Symbol : SkoarToke {
    classvar <regex = "[\\\\@][a-zA-Z_][a-zA-Z0-9_]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Symbol);
    }

}

Toke_Deref : SkoarToke {
    classvar <regex = "!(?![!}]|=)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Deref);
    }

}

Toke_Rep : SkoarToke {
    classvar <regex = "%+";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Rep);
    }

}

Toke_DaCapo : SkoarToke {
    classvar <regex = "D\\.C\\.|Da Capo";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_DaCapo);
    }

}

Toke_SymbolName : SkoarToke {
    classvar <regex = "[a-zA-Z_][a-zA-Z0-9_]*";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_SymbolName);
    }

}

Toke_Crotchets : SkoarToke {
    classvar <regex = "[}]+\\.?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Crotchets);
    }

}

Toke_Semicolon : SkoarToke {
    classvar <regex = ";";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Semicolon);
    }

}

Toke_SkoarpionStart : SkoarToke {
    classvar <regex = "[{]!";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_SkoarpionStart);
    }

}

Toke_ListSep : SkoarToke {
    classvar <regex = ",";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_ListSep);
    }

}

Toke_OttavaA : SkoarToke {
    classvar <regex = "8va|ottava (alta|sopra)|all' ottava";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_OttavaA);
    }

}

Toke_NamedNoat : SkoarToke {
    classvar <regex = "(?:_?)(?:[a-eg]|f(?![ac-zA-Z_]))(#*|b*)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_NamedNoat);
    }

}

Toke_OctaveShift : SkoarToke {
    classvar <regex = "~+o|o~+";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_OctaveShift);
    }

}

Toke_Bars : SkoarToke {
    classvar <regex = ":?\\|+:?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Bars);
    }

}

Toke_MsgNameWithArgs : SkoarToke {
    classvar <regex = "[a-zA-Z_][a-zA-Z0-9_]*<";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_MsgNameWithArgs);
    }

}

Toke_Coda : SkoarToke {
    classvar <regex = "\\([+]\\)";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Coda);
    }

}

Toke_Carrot : SkoarToke {
    classvar <regex = "\\^(?!\\^[(])";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Carrot);
    }

}

Toke_RWing : SkoarToke {
    classvar <regex = "[)]\\^\\^";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_RWing);
    }

}

Toke_Tuplet : SkoarToke {
    classvar <regex = "/\\d+(:\\d+)?|(du|tri|quadru)plets?|(quin|sex|sep|oc)tuplets?";

    *match {
        | buf, offs |

        ^SkoarToke.match_toke(buf, offs, Toke_Tuplet);
    }

}

