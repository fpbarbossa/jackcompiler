# ==========================================
# 1. SCANNER (Analisador Léxico)
# ==========================================
class Token:
    def __init__(self, type_, value):
        self.type = type_
        self.value = value

    def __repr__(self):
        return f"Token({self.type}, {repr(self.value)})"

class Scanner:
    def __init__(self, source):
        self.source = source
        self.pos = 0
        self.current_char = self.source[self.pos] if self.pos < len(self.source) else None

    def advance(self):
        self.pos += 1
        self.current_char = self.source[self.pos] if self.pos < len(self.source) else None

    def skip_whitespace(self):
        while self.current_char is not None and self.current_char.isspace():
            self.advance()

    def get_next_token(self):
        while self.current_char is not None:
            if self.current_char.isspace():
                self.skip_whitespace()
                continue
            
            if self.current_char.isdigit():
                num_str = ''
                while self.current_char is not None and self.current_char.isdigit():
                    num_str += self.current_char
                    self.advance()
                return Token('NUM', int(num_str))
                
            if self.current_char.isalpha() or self.current_char == '_':
                id_str = ''
                while self.current_char is not None and (self.current_char.isalnum() or self.current_char == '_'):
                    id_str += self.current_char
                    self.advance()
                if id_str == 'let': return Token('LET', 'let')
                if id_str == 'print': return Token('PRINT', 'print')
                return Token('ID', id_str)
                
            symbols = {'+': 'PLUS', '-': 'MINUS', '=': 'ASSIGN', ';': 'SEMI'}
            if self.current_char in symbols:
                char = self.current_char
                self.advance()
                return Token(symbols[char], char)
            
            raise Exception(f'Erro Léxico: Caractere inválido "{self.current_char}"')
            
        return Token('EOF', 'EOF')