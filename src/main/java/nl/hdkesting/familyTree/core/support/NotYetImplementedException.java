package nl.hdkesting.familyTree.core.support;

public class NotYetImplementedException extends RuntimeException
{
    // https://stackoverflow.com/a/50461711/121309

    /**
     * @deprecated Deprecated to remind you to implement the corresponding code
     *             before releasing the software.
     */
    @Deprecated
    public NotYetImplementedException()
    {
    }

    /**
     * @deprecated Deprecated to remind you to implement the corresponding code
     *             before releasing the software.
     */
    @Deprecated
    public NotYetImplementedException(String message)
    {
        super(message);
    }
}