package edu.ehu.galan.cvalue;
/*
 *    ProcessLinguisticFilters.java
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


import com.google.common.base.CharMatcher;
import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class take the tokenized text, and process it through the desired filters
 *
 * @author Angel Conde Manjon
 */
public class ProcessLinguisticFilters {

    private List<LinkedList<Token>> text;
    private final List<ILinguisticFilter> filterList;
    private List<Candidate> candidates;
    private final Logger logger=LoggerFactory.getLogger(this.getClass());
    /**
     * Main constructor
     */
    public ProcessLinguisticFilters() {
        filterList = new ArrayList<>();
    }

    /**
     * adds a linguistic filter to be processed
     *
     * @param filter
     */
    public void addFilter(ILinguisticFilter filter) {
        filterList.add(filter);
    }

    /**
     * takes a tokenized text, and get a list of candidates processing all the linguistic filters
     *
     * @param text
     * @return
     */
    public List<Candidate> processText(List<LinkedList<Token>> text) {
        candidates = new ArrayList<>();
        for (LinkedList<Token> linkedList : text) {
            for (ILinguisticFilter filter : filterList) {
                List<String> tempCandidates = filter.getCandidates(linkedList);
                for (String string : tempCandidates) {
//                    System.out.println(string);
                    Candidate cand = new Candidate(string, string.split("\\s").length);
                    candidates.add(cand);
                }
            }
        }
        List<Candidate> cleaned = cleanCandidates(candidates);
        Collections.sort(cleaned,(cand1,cand2) -> cand1.getLenght()> cand2.getLenght()? -1 : cand1.getLenght() == cand2.getLenght() ? 0 : 1);
        return cleaned;
    }

    private List<Candidate> cleanCandidates(List<Candidate> candidates) {
        List<Candidate> cand = new ArrayList<>();
        for (Candidate candidate : candidates) {
            String printable = CharMatcher.INVISIBLE.removeFrom(candidate.getText());
            int size = candidate.getText().length();
            if (printable.length() > 0) {
                if (!cand.contains(candidate)) {
                    candidate.incrementFreq(1);
                    cand.add(candidate);
                } else {
                    int idx = cand.indexOf(candidate);
                    cand.get(idx).incrementFreq(1);
                }
            }
        }
        if(cand.size()>60000){
         logger.warn("The candidates of CValue are > 5.000 .... an empty list will be returned");
         List<Candidate> cands=new ArrayList<>();
         return cands;
        }
        return cand;
    }

    public int getNumberOfFilters() {
        return filterList.size();
    }
}
