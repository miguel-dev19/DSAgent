#!/usr/bin/env python3
"""
Script para probar las respuestas de la API de DeepSeek.
Muestra el texto crudo tal cual lo recibe la app.
"""

import requests
import json

API_URL = "https://ds-flaskapi.onrender.com"
TIMEOUT = 120

def test_response(prompt="Hola, explicame que es Kotlin brevemente"):
    """Prueba una respuesta completa de la API."""
    
    print("=" * 60)
    print(" CREANDO SESION...")
    print("=" * 60)
    
    # 1. Crear sesion
    resp = requests.post(f"{API_URL}/api/session", timeout=TIMEOUT)
    session_id = resp.json()['session_id']
    print(f"Session ID: {session_id}\n")
    
    # 2. Enviar mensaje
    print("=" * 60)
    print(f" PREGUNTA: {prompt}")
    print("=" * 60)
    
    payload = {
        "session_id": session_id,
        "prompt": prompt,
        "thinking_enabled": True,
        "search_enabled": True
    }
    
    think_text = []
    response_text = []
    
    with requests.post(
        f"{API_URL}/api/chat",
        json=payload,
        stream=True,
        timeout=TIMEOUT
    ) as r:
        
        event_type = None
        
        for line in r.iter_lines(decode_unicode=True):
            if not line:
                continue
            
            if line.startswith('event: '):
                event_type = line[7:].strip()
                continue
            
            if line.startswith('data: '):
                data_str = line[6:].strip()
                
                if not data_str or data_str == '""' or data_str == '"':
                    continue
                
                try:
                    data = json.loads(data_str)
                except json.JSONDecodeError:
                    data = data_str
                
                if event_type == 'think':
                    think_text.append(str(data))
                    # Mostrar en azul
                    print(f"\033[94m{data}\033[0m", end="", flush=True)
                
                elif event_type == 'response':
                    response_text.append(str(data))
                    # Mostrar en verde
                    print(f"\033[92m{data}\033[0m", end="", flush=True)
                
                elif event_type == 'done':
                    print("\n")
                    break
    
    # 3. Mostrar respuesta completa
    full_response = "".join(response_text)
    full_think = "".join(think_text)
    
    print("\n" + "=" * 60)
    print(" RESPUESTA COMPLETA (CRUDA)")
    print("=" * 60)
    print(full_response)
    
    print("\n" + "=" * 60)
    print(" CONTIENE EMOJIS?")
    print("=" * 60)
    
    # Detectar emojis
    emoji_pattern = re.compile(
        "[\U0001F600-\U0001F64F"
        "\U0001F300-\U0001F5FF"
        "\U0001F680-\U0001F6FF"
        "\U0001F1E0-\U0001F1FF"
        "\u2600-\u26FF"
        "\u2700-\u27BF"
        "]+", flags=re.UNICODE
    )
    
    emojis = emoji_pattern.findall(full_response)
    if emojis:
        print(f"EMOJIS ENCONTRADOS: {emojis}")
    else:
        print("No se encontraron emojis")
    
    print("\n" + "=" * 60)
    print(" CONTIENE FINISHED?")
    print("=" * 60)
    if "FINISHED" in full_response.upper():
        print("SI contiene FINISHED")
    else:
        print("No contiene FINISHED")
    
    print("\n" + "=" * 60)
    print(" CONTIENE BLOQUES DE CODIGO?")
    print("=" * 60)
    if "```" in full_response:
        print("SI contiene bloques de codigo")
        # Extraer bloques
        import re
        blocks = re.findall(r'```(\w*)\n(.*?)```', full_response, re.DOTALL)
        for i, (lang, code) in enumerate(blocks):
            print(f"\n  Bloque {i+1} ({lang}):")
            print(f"  {code[:100]}...")
    else:
        print("No contiene bloques de codigo")
    
    print("\n" + "=" * 60)
    print(" CONTIENE MARKDOWN?")
    print("=" * 60)
    patterns = {
        "**negrita**": r'\*\*.*?\*\*',
        "*cursiva*": r'\*.*?\*',
        "`codigo`": r'`.*?`',
        "[enlace](url)": r'\[.*?\]\(.*?\)',
        "# Titulo": r'^#+\s',
        "- Lista": r'^\-|\*\s',
    }
    
    for name, pattern in patterns.items():
        matches = re.findall(pattern, full_response, re.MULTILINE)
        if matches:
            print(f"  {name}: {len(matches)} ocurrencias")
    
    return full_response

if __name__ == "__main__":
    import sys
    import re
    
    prompt = sys.argv[1] if len(sys.argv) > 1 else "Hola, explicame que es Kotlin brevemente"
    test_response(prompt)
