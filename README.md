# Nand2Tetris — Full VM Translator (Java)

Java implementation of the full VM Translator from the [Nand2Tetris](https://www.nand2tetris.org/) course (Projects 7 
& 8).  
Translates `.vm` Virtual Machine code into Hack Assembly `.asm`, supporting **all VM commands** including function 
calls and returns.

## Features
- Parses and translates:
  - Arithmetic & logical commands
  - Memory access (push/pop)
  - Program flow (`label`, `goto`, `if-goto`)
  - Function calls & returns (`function`, `call`, `return`)
- Supports single `.vm` files and directories containing multiple `.vm` files
- Produces valid `.asm` output for the Hack platform
- Includes a `Makefile` and a `VMtranslator` shell script for easy execution
- Java 8+ compatible, no external dependencies

## Build & Run

### Using Makefile & VMtranslator script
```bash
# Compile the project
make

# Run the translator on a single file
./VMtranslator path/to/YourFile.vm

# Or run on a directory with multiple VM files
./VMtranslator path/to/Directory

## Notes
- This project passed all official Nand2Tetris VM translator tests.
- Works with any Java 8+ installation.
- No external dependencies required.
