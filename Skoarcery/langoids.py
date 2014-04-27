def init():
    print("langoids initialized.")


class Production:

    def __init__(self, name, list_of_langoids):
        from Skoarcery.tokens import Empty

        self.name = name
        self.production = list_of_langoids
        self.derives_empty = list_of_langoids[0] == Empty

    def __str__(self):
        s = self.name + " ->"

        for alpha in self.production:
            s += " " + alpha.name

        return s


class Langoid:

    def __init__(self, name):
        self.name = name
        self.derives_empty = False

    def __hash__(self):
        return hash(self.name)

    def __eq__(self, other):
        return self.name == other.name

    def __str__(self):
        return self.name

    def __repr__(self):
        return self.name

    def __index__(self):
        return self.name


class Terminal(Langoid):

    def __init__(self, name, regex):
        super().__init__(name)

        self.regex = regex

        if name == "<e>":
            self.derives_empty = True

    def __str__(self):
        return "T_" + self.name


class Nonterminal(Langoid):

    def __init__(self, name):
        super().__init__(name)

        # list of productions
        self.production_rules = []
        self.first = set()
        self.follow = set()
        self.derives_empty = False

    def add_production(self, p):

        o = Production(self.name, p)

        if o.derives_empty:
            self.derives_empty = True

        self.production_rules.append(o)

    def __repr__(self):
        return "N_" + self.name


