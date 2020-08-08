# KMP-algorithm-Stage-2
Description
Improve your file type checker’s pattern match logic by rewriting the pattern search algorithm. Use some of the advanced algorithms you have learned so far like the Knuth-Morris-Pratt algorithm. Check your program’s search performance with some huge file. Compare your improved search engine with a naive implementation. If you’re a Linux user, you can use "time" for measuring execution time, (see man time for details). Otherwise, you can use nanoTime() from the java.lang.System class. Check the official documentation.

Your program should accept another argument, that represents an algorithm - a naive implementation that you implemented in the previous step marked as --naive and KMP algorithm marked as --KMP. Other arguments should be parsed after this one - file name, pattern and file type.

Output examples
First, we checked the naive implementation, and it took roughly 5 seconds. Then KMP showed 5 times better performance. You should expect similar behavior from your program.

java Main --naive huge_doc.pdf "%PDF-" "PDF document"
PDF document
It took 5.011 seconds
java Main --KMP huge_doc.pdf "%PDF-" "PDF document"
PDF document
It took 1.037 seconds
java Main --naive pic.png "%PDF-" "PDF document"
Unknown file type
It took 3.641 seconds
java Main --KMP pic.png "%PDF-" "PDF document"
Unknown file type
It took 0.469 seconds
