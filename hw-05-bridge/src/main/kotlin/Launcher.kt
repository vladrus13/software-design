import java.nio.file.Path

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        Instructor.allRun()
    } else {
        if (args.size != 4) {
            println("Usage: <path-to-file> <destination> <drawing-type> <graph-type>")
            return
        }
        Instructor.adapt(Path.of(args[0]), Path.of(args[1]), args[2], args[3])
    }
}