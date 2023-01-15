with open("Syntax.in", 'r') as fin:
    with open("g2.txt", 'w') as fout:

        nonterminals = set()
        alphabet = set()

        for line in [x.strip() for x in fin.readlines()]:
            if len(line) == 0:
                continue
            
            
            line = line.replace("<", " ")
            line = line.replace(">", " ")
            
            lhs, rhs = [x.strip() for x in line.split("::=")]

            lhs = [x.strip() for x in lhs.split(" ") if len(x) > 0]
            rhs = [x.strip() for x in rhs.split("|") if len(x.strip()) > 0]

            for nont in lhs:
                nonterminals.add(nont)

            for result in rhs:
                for x in [x.strip() for x in result.split(" ") if len(x.strip())>0]:
                    alphabet.add(x)

            for result in rhs:
                lhs_str = " ".join(lhs)
                rhs_str = " ".join([x.strip() for x in result.split(" ") if len(x.strip()) > 0])
                fout.write(f"{lhs_str} -> {rhs_str}\n")

            # print(rhs)

        
        print(*nonterminals, sep=" ")
        print(*(list(sorted([x for x in alphabet if x not in nonterminals]))), sep=" ")

