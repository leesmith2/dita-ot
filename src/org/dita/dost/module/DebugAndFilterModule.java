/*
 * (c) Copyright IBM Corp. 2004, 2005 All Rights Reserved.
 */
package org.dita.dost.module;

import java.io.File;
import java.util.LinkedList;

import org.dita.dost.exception.DITAOTException;
import org.dita.dost.pipeline.AbstractPipelineInput;
import org.dita.dost.pipeline.AbstractPipelineOutput;
import org.dita.dost.pipeline.PipelineHashIO;
import org.dita.dost.reader.DitaValReader;
import org.dita.dost.reader.ListReader;
import org.dita.dost.util.Constants;
import org.dita.dost.writer.DitaWriter;


/**
 * DebugAndFilterModule implement the second step in preprocess. It will insert debug
 * information into every dita files and filter out the information that is not 
 * necessary.
 * 
 * @author Zhang, Yuan Peng
 */
public class DebugAndFilterModule extends AbstractPipelineModule {

    /**
     * Automatically generated constructor: DebugAndFilterModule
     */
    public DebugAndFilterModule() {
    }

    
    /**
     * @see org.dita.dost.module.AbstractPipelineModule#execute(org.dita.dost.pipeline.AbstractPipelineInput)
     * 
     */
    public AbstractPipelineOutput execute(AbstractPipelineInput input) throws DITAOTException {
        String baseDir = ((PipelineHashIO) input).getAttribute(Constants.ANT_INVOKER_PARAM_BASEDIR);
        String ditavalFile = ((PipelineHashIO) input).getAttribute(Constants.ANT_INVOKER_PARAM_DITAVAL);
        String tempDir = ((PipelineHashIO) input).getAttribute(Constants.ANT_INVOKER_PARAM_TEMPDIR);
        String filePathPrefix = null;
        ListReader listReader = new ListReader();
        LinkedList parseList = null;
        Content content;
        DitaWriter fileWriter;
        
        if (baseDir == null){
        	throw new DITAOTException(
				"Please specify the base directory.");
        }
        if (tempDir == null){
        	throw new DITAOTException(
				"Please specify the temp directory.");
        }
        
        listReader.read(tempDir + File.separator + Constants.FILE_NAME_DITA_LIST);
        parseList = (LinkedList) listReader.getContent()
                .getCollection();
        if (ditavalFile!=null){
            DitaValReader filterReader = new DitaValReader();
            filterReader.read(ditavalFile);
            content = filterReader.getContent();
        }else{
            content = new ContentImpl();
        }

        fileWriter = new DitaWriter();
        content.setValue(tempDir);
        fileWriter.setContent(content);
        
        if(baseDir!=null){
            filePathPrefix = baseDir + Constants.STICK;
        }
        
        while (!parseList.isEmpty()) {
            /*
             * Usually the writer's argument for write() is used to pass in the
             * ouput file name. But in this case, the input file name is same as
             * output file name so we can use this argument to pass in the input
             * file name. "|" is used to separate the path information that is
             * not necessary to be kept (baseDir) and the path information that
             * need to be kept in the temp directory.
             */
        	fileWriter.write(
        			new StringBuffer().append(filePathPrefix)
        				.append((String) parseList.removeLast()).toString());
            
        }
        

        return null;
    }

}
