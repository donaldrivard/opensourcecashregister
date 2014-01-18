/*
 * Open Source Cash Register
 * 
 * Copyright (C) 2013-2014 Bernhard Streit
 * 
 * This file is part of the Open Source Cash Register program.
 * 
 * Open Source Cash Register is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 * 
 * Open Source Cash Register is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *  
 * --
 *  
 * See /licenses/gpl-3.txt for a copy of the GNU GPL.
 * See /README.txt for more information about the software and the author(s).
 * 
 */
package de.bstreit.java.oscr.business.user;

import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

import de.bstreit.java.oscr.business.base.persistence.AbstractPersistentObjectWithContinuance;


/**
 * 
 * @author streit
 */
@Entity
public class User extends AbstractPersistentObjectWithContinuance<User> {

  /**
   * The name <b>has</b> to be unique, at least during the period of time this
   * entity is valid.
   */
  @NaturalId
  private String name;


  public User() {
  }

  public User(String name) {
    this.name = name;
  }


  @Override
  protected void additionalEqualsForSubclasses(EqualsBuilder equalsBuilder, User otherObject) {
    equalsBuilder.append(name, otherObject.name);
  }

  @Override
  protected void additionalHashcodeForSubclasses(HashCodeBuilder builder) {
    builder.append(name);
  }


  /**
   * @return the {@link #name}
   */
  public String getName() {
    return name;
  }


  /**
   * @param name
   *          the {@link #name} to set
   */
  public void setName(String name) {
    this.name = name;
  }


}
