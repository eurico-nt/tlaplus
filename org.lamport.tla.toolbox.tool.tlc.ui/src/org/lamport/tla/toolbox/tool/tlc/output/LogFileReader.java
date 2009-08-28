package org.lamport.tla.toolbox.tool.tlc.output;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.lamport.tla.toolbox.tool.tlc.output.source.ITLCOutputSource;
import org.lamport.tla.toolbox.tool.tlc.output.source.TagBasedTLCOutputIncrementalParser;
import org.lamport.tla.toolbox.tool.tlc.ui.TLCUIActivator;

/**
 * Reads the log file and parses the content
 * @author Simon Zambrovski
 * @version $Id$
 */
public class LogFileReader
{
    private TagBasedTLCOutputIncrementalParser parser;
    private IFile logFile;

    public LogFileReader(String name, IFile logFile)
    {
        this.logFile = logFile;
        this.parser = new TagBasedTLCOutputIncrementalParser(name, ITLCOutputSource.PRIO_LOW);
    }

    /**
     * Reads the contents
     */
    public void read()
    {
        try
        {
            FileEditorInput fileEditorInput = new FileEditorInput(logFile);
            FileDocumentProvider fileDocumentProvider = new FileDocumentProvider();
            fileDocumentProvider.connect(fileEditorInput);
            IDocument document = fileDocumentProvider.getDocument(fileEditorInput);
            this.parser.addIncrement(document.get());
            this.parser.done();
        } catch (CoreException e)
        {
            TLCUIActivator.logError("Error accessing the TLC log file contents", e);
        } catch (BadLocationException e)
        {
            TLCUIActivator.logError("Error positioning in the TLC log file", e);
        }

    }

    public ITLCOutputSource getSource()
    {
        return this.parser.getSource();
    }

}
