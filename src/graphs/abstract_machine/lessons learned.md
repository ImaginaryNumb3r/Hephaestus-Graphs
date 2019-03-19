Notes: Implications of using push pipes vs. pull pipes

Push: Must preserve state, but can gradually mutate over time.
Keep it simple: No fancy sub-machines. Keep your eye on the problem.
It is worth to keep around convenience methods/classes when they (partially) erase generics for the most common use-case(s).

Problems: Did not consider implications of using collections as inputs.
Exploring other use-cases could have been worthwhile.
Nonetheless, it was a very sufficient initial version and in step 2, I will determine how expendable the current code base is.

Learned: Working with state patterns brings simplicity into complex processes.
Writing down the conditional flow of your program can bring clarity when working with situations that needs to consider many circumstances/exceptions/edge-cases.

On second thought: Push and pull have different usecases.
Push:   You delegate internal consistency to the user. You need to handle expected and illegal state yourself.

Pull:   You give the machine all responsibility. The user is responsible for creating appropriate rules that cover all cases.
        However, you cannot create decisions on the fly. It is also more limited in terms of use-cases, since it will consume all of the iterable until the buffer is used up or an exception is thrown.
        This makes a pull-pipe unusable when a
^