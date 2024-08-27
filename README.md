# piano-noteblock

A Minecraft mod to connect your MIDI piano to Minecraft.

## WIP
This project is a work in progress! \
It is currently functional, but expect bugs and cryptic problems

### Usage
To tune your noteblocks for usage, run `/pnbs tune <octaves>`. \
The `octaves` parameter specifies the amount of  *note block octaves* (24 notes) \
you would like to use. Note that this definition is different from musical octaves.

Next, plug in a MIDI keyboard and run `/pnbs scan` to scan for MIDI devices. \
Your keyboard should show up in the chat.

Finally, play some notes! If the note mapping from Minecraft is \
too high or too low, you can change your offset using `/pnbs offset <offset>`.

### Mapping
Your offset will depend on where you want to map the notes into Minecraft. \
MIDI takes in notes starting from A0, which in Minecraft will be the lowest \
achievable note (F#1). If you would like to change this mapping for any reason, \
run `/pnbs offset` to offset this mapping. For example, if you would like C5 in \
Minecraft to be C4 on your piano, run `/pnbs offset 3`. 