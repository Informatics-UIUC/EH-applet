/**
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2008, Board of Trustees-University of Illinois.
 * All rights reserved.
 *
 * Developed by:
 *
 * Automated Learning Group
 * National Center for Supercomputing Applications
 * http://www.seasr.org
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal with the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimers.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimers in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the names of Automated Learning Group, The National Center for
 *    Supercomputing Applications, or University of Illinois, nor the names of
 *    its contributors may be used to endorse or promote products derived from
 *    this Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * WITH THE SOFTWARE.
 */

package ncsa.evolutionhighway.components.support;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DelimitedFileParser {

    //~ Instance fields *********************************************************

    /** Description of field hasLabels. */
    private boolean hasLabels = true;

    /** Description of field hasSpecDelim. */
    private boolean hasSpecDelim = false;

    /** Description of field hasTypes. */
    private boolean hasTypes = true;

    /** Description of field labelsRow. */
    private int labelsRow;

    /** Description of field specDelim. */
    private String specDelim = null;

    /** Description of field typesRow. */
    private int typesRow;

    private Logger _logger;

    //~ Methods *****************************************************************

    /**
     * Description of method getHasLabels.
     *
     * @return Description of return value.
     */
    public boolean getHasLabels() { return hasLabels; }

    /**
     * Description of method getHasSpecDelim.
     *
     * @return Description of return value.
     */
    public boolean getHasSpecDelim() { return hasSpecDelim; }

    /**
     * Description of method getHasTypes.
     *
     * @return Description of return value.
     */
    public boolean getHasTypes() { return hasTypes; }

    /**
     * Description of method getLabelsRow.
     *
     * @return Description of return value.
     */
    public int getLabelsRow() { return labelsRow; }

    /**
     * Description of method getSpecDelim.
     *
     * @return Description of return value.
     */
    public String getSpecDelim() { return specDelim; }

    /**
     * Description of method getTypesRow.
     *
     * @return Description of return value.
     */
    public int getTypesRow() { return typesRow; }

    /**
     * Description of method setHasLabels.
     *
     * @param b Description of parameter b.
     */
    public void setHasLabels(boolean b) { hasLabels = b; }

    /**
     * Description of method setHasSpecDelim.
     *
     * @param b Description of parameter b.
     */
    public void setHasSpecDelim(boolean b) { hasSpecDelim = b; }

    /**
     * Description of method setHasTypes.
     *
     * @param b Description of parameter b.
     */
    public void setHasTypes(boolean b) { hasTypes = b; }

    /**
     * Description of method setLabelsRow.
     *
     * @param i Description of parameter i.
     */
    public void setLabelsRow(int i) { labelsRow = i; }

    /**
     * Description of method setSpecDelim.
     *
     * @param s Description of parameter s.
     */
    public void setSpecDelim(String s) { specDelim = s; }

    /**
	 * Description of method setTypesRow.
	 *
	 * @param i Description of parameter i.
	 */
	public void setTypesRow(int i) { typesRow = i; }

	public void initialize() {
	    _logger = Logger.getLogger(DelimitedFileParser.class.getName());

	    try {
	    	labelsRow = 0;
	    	typesRow = 1;

	    	String strDelim = "\t";
	    	if (strDelim.equals("default")) {
	    		setHasSpecDelim(false);
	    		setSpecDelim(null);
	    	} else {
	    		setSpecDelim(strDelim);
	    		setHasSpecDelim(true);
	    	}
	    }
	    catch (Exception e) {
	    	_logger.log(Level.SEVERE, "Initialize error", e);
	    	throw new RuntimeException(e);
	    }
	}

	public DelimitedFileParserURL execute(File file) throws Exception {
		_logger.entering(this.getClass().getName(), "execute");

	    //DataObjectProxy dataobj = (DataObjectProxy) context.getDataComponentFromInput(DATA_INPUT_DATAOBJECTPROXY);


	    DelimitedFileParserURL df = null;

	    int lbl = -1;

	    if (getHasLabels()) {
	        lbl = getLabelsRow();
	    }

	    int typ = -1;

	    if (getHasTypes()) {
	        typ = getTypesRow();
	    }

	    if (!getHasSpecDelim()) {
	        try {
	            df = new DelimitedFileParserURL(file, lbl, typ);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    } else {
	        String s = getSpecDelim();
	        char[] del = s.toCharArray();
	        //System.out.println("delimiter is: " + del[0]);

	        if (del.length == 0) {
	            throw new Exception("User specified delimiter has not been set");
	        }

	        try {
	            df = new DelimitedFileParserURL(file, lbl, typ, del[0]);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }


	    return df;
	}

	public void dispose() {
	}
}
