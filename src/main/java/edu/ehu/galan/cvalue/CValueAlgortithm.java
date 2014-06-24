package edu.ehu.galan.cvalue;

/*
 *    CValueAlgortithm.java
 *    Copyright (C) 2013 Angel Conde, neuw84 at gmail dot com
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.AbstractAlgorithm;
import edu.ehu.galan.cvalue.model.Document;
import edu.ehu.galan.cvalue.model.Term;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CValue implementation based on Frantzi, K., Ananiadou, S. and Mima, H. (2000)
 * Automatic recognition of multi-word terms. International Journal of Digital
 * Libraries 3(2), pp.117-132.
 *
 *
 * @author Angel Conde Manjon
 */
public class CValueAlgortithm extends AbstractAlgorithm {

    private transient Document doc = null;
    private transient ProcessLinguisticFilters filters = null;
    private transient List<Term> termList; //gson 

    /**
     *
     */
    public CValueAlgortithm() {
        super(true, "CValue");
        filters = new ProcessLinguisticFilters();
        termList = super.getTermList();
    }

    @Override
    public void init(Document pDoc, String pPropsDir) {
        setDoc(pDoc);
    }
    
    /**
     * Sets the document to be analyzed
     * @param pDoc
     */
    public void init(Document pDoc) {
        setDoc(pDoc);
    }

    /**
     *
     * @param pFilter
     */
    public void addNewProcessingFilter(ILinguisticFilter pFilter) {
        filters.addFilter(pFilter);
    }

    @Override
    public void runAlgorithm() {
        if (filters.getNumberOfFilters() > 0) {
            List<Candidate> candList = filters.processText(doc.getTokenList());
            CValue cvalue = new CValue(candList);
            cvalue.processCValue();
            candList = cvalue.getCandList();
            List<Term> termLi = candList.parallelStream().map(cand -> new Term(cand.getText().trim(),cand.getCValue())).sorted((o1, o2) -> o1.getScore() > o2.getScore() ? -1 : o1.getScore() == o2.getScore() ? 0 : 1).collect(Collectors.toList());
            doc.setTermList(termLi);
//            super.setTermList(termLi);
//            this.saveToTmp();
        } else {
            System.out.println("A lingustic filter is needed for CValue algorithm");
        }
    }

    /**
     *
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

}
