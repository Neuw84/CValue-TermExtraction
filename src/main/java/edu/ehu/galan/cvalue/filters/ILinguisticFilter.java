package edu.ehu.galan.cvalue.filters;

/*
 *    ILinguisticFilter.java
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


import edu.ehu.galan.cvalue.model.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Each Linguistic filter must implement this interface
 * @author Angel Conde Manjon
 */
public interface ILinguisticFilter {
    
    
    /**
     * each filter gets a sentence (Tokens) and returns a list of candidates (list of strings)
     * @param pSentence
     * @return
     */
    public List<String> getCandidates(LinkedList<Token> pSentence);
  
    
    
}
