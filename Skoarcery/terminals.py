src = """
#
# Skoar Tokes
#
# format (at very start of line): TokeName: regex
#
# If token carries information: TokeName*: regex
#   -  be sure an inspector for TokeName exists
#

<e>:            unused
EOF:            unused
Whitespace:     [ \\t]*
Newline:        [\\n\\r][\\n\\r \\t]*

True:           yes|true
False:          no|false
Cat:            =\\^\\.\\^=

Voice*:         \\.(([a-zA-Z_][a-zA-Z0-9_]*)?|\\.+)

Comment:        <[?](.|[\\n\\r])*?[?]>

# careful not to match ottavas ending in (ma,mb,va,vb), or steal from floats
Int*:           (-)?(0|[1-9][0-9]*)(?![0-9]*Hz|[mv][ab]|\\.[0-9]|/)
Float*:         (-)?(0|[1-9][0-9]*)\\.[0-9]+(?!Hz)

Freq*:          (0|[1-9][0-9]*)(\\.[0-9]+)?Hz

Meter*:         [1-9][0-9]*/[1-9][0-9]*

ArgSpec:        <[a-zA-Z]+(,[a-zA-Z]+)*>

ListS:          <(?![=?])|<(?=[=]\\^\\.)
ListE:          >(?![=])
ListSep:        ,

# one ^ but don't eat ^^( which is cthulhu's left wing
Carrot*:        \\^(?!\\^[(])
LWing:          \\^\\^[(]
RWing:          [)]\\^\\^

Tuplet*:        /\\d+(:\\d+)?|(du|tri|quadru)plets?|(quin|sex|sep|oc)tuplets?
Crotchets*:     [}]+\\.?
Quavers*:       o+/\\.?

Quarters*:      \\.?[)]+(?:__?)?\\.?
Eighths*:       \\.?\\]+(?:__?)?\\.?

Caesura:        //
Slash:          /(?![/0-9])

HashLevel:      \\[#*[ ]*\\]


# we can't allow f for forte as f is a noat, so we allow
#
#  forte fforte ffforte ff fff, but not f
#
#  for consisentecy, piano, ppiano, pppiano work too.
#
#  default velocity:
#    ppp (16), pp (32), p (48), mp (64), mf (80), f (96), ff (112), fff (127)

DynPiano*:        (m(ezzo)?p|p+)(iano)?
DynForte*:        m(ezzo)?f(orte)?|f+orte|ff+
DynSFZ:           sfz
DynFP:            fp

AssOp:            =>|[+]>|->|[*]>
MsgOp:            \\.(?![)\\]])
MathOp:           [+*\\-](?!>)

NamedNoat*:       (?:_?)(?:[a-g](?![ac-zA-Z_]))(#|b)?
Choard*:          ~*[ABCDEFG](?![.ce-ln-rt-zA-LN-Z]|a[l ])(#|b)?([Mm0-9]|sus|dim|aug|dom)*(/[A-G][#b]?)?~*

BooleanOp*:       ==|!=|<=|>=|and|or|xor
CondS:            [{][?][\\n]*
CondIf:           [?][?](?![}])
CondE:            [?][}]
Semicolon:        ;

CutsS:            [{]=[\\n]*
CutsE:            =[}]

LoopS:            [{]:[\\n]*
LoopE:            :[}]
LoopSep:          ::[\\n]*(?![|])

Fairy:            [$]

# we do this, because skoaroids can follow skoaroids.
MsgName*:         [a-zA-Z_][a-zA-Z0-9_]*(?!<)
MsgNameWithArgs*: [a-zA-Z_][a-zA-Z0-9_]*<

Symbol*:          [\\\\@][a-zA-Z0-9_][a-zA-Z0-9_]*
SymbolName*:      [a-zA-Z0-9_][a-zA-Z0-9_]*
SymbolColon*:     [a-zA-Z0-9_][a-zA-Z0-9_]*[ \\t]*:(?![:|}])


SkoarpionStart:   [{]!
SkoarpionEnd:     ![}]
SkoarpionSep:     !!
Deref:            !(?![!}]|=)

Nosey:            ,

DaCapo:           D\\.C\\.|Da Capo
DalSegno:         D\\.S\\.|Dal Segno
Fine:             fine
Segno*:           ,[Ss](?:egno)?`(?:[a-zA-Z_][a-zA-Z0-9_]*`)*
Coda:             \\([+]\\)(?:`(?:[a-zA-Z_][a-zA-Z0-9_]*`)*)?
Rep*:             %+
AlCoda:           al(la)? coda
AlSegno:          al segno
AlFine:           al fine

UGen:             u[A-Z][A-Za-z0-9_]*
OctaveShift*:     ~+o|o~+

OttavaA:          8va|ottava (alta|sopra)|all' ottava
OttavaB:          8vb|ottava (bassa|sotto)

QuindicesimaA:    15ma|alla quindicesima
QuindicesimaB:    15mb

Portamento:       port\\.?
Loco:             loco
Volta*:           \\[\\d+\\.\\]

# TODO: deal with \"
String*:          \'[^']*\'

Bars*:            :?\\|+:?

Times:            [Tt]imes
"""

#
#
#

list_of_names = None
inspectables = None
tokens = None
Empty = None
EOF = None
Whitespace = None

odd_balls = None


def init():
    from Skoarcery.langoids import Terminal
    global src, list_of_names, tokens, EOF, Empty, Whitespace, odd_balls, inspectables

    list_of_names = []
    inspectables = []
    tokens = dict()

    for token_line in src.split("\n"):

        token_line = token_line.strip()
        if len(token_line) > 0 and not token_line.startswith("#"):

            (token, v, regex) = token_line.partition(":")

            token = token.strip()
            regex = regex.strip()

            if token.endswith("*"):
                token = token.rstrip("*")
                inspectables.append(token)

            list_of_names.append(token)

            tokens[token] = Terminal(token, regex)

    #print("# tokens initialized.")

    Empty = Terminal("<e>", None)
    EOF = Terminal("EOF", None)
    Whitespace = tokens["Whitespace"]

    odd_balls = {Empty, EOF, Whitespace}


