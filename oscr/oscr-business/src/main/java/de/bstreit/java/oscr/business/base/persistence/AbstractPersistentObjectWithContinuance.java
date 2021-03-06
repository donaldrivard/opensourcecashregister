/*
 * Open Source Cash Register
 * 
 * Copyright (C) 2013, 2014 Bernhard Streit
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
 * --------------------------------------------------------------------------
 *  
 * See oscr/licenses/gpl-3.txt for a copy of the GNU GPL.
 * See oscr/README.txt for more information about the software and the author(s).
 * 
 */
package de.bstreit.java.oscr.business.base.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

/**
 * <p>
 * Superclass for persistent objects that are valid within a certain time range.
 * For example, consider the change of the price of an offer in the course of
 * time:
 * </p>
 * 
 * <pre>
 * Espresso: From 01.01.2010 00:00 - 31.05.2010 23:59:59.9999: 1,10 EUR
 * Espresso: From 01.06.2010 00:00 - 31.05.2011 23:59:59.9999: 1,20 EUR
 * Espresso: From 01.06.2011 00:00 - null (== current): 1,30 EUR
 * </pre>
 * 
 * <p>
 * Objects of this type are expected to be immutable on the {@link #validFrom}
 * field, and on at least one additional identifier which together form the
 * natural key of those objects. For example, there cannot be two espresso
 * offers with different prices at the same time; but it is allowed that an
 * espresso and an cappuccino offer exist simultaneously. The fields forming the
 * natural key (including {@link #validFrom}) are used in equals and hashcode.
 * </p>
 * 
 * <p>
 * The {@link #validTо} field is allowed to change only once from
 * <code>null</code> to a non-null {@link Date}). This can be seen as archiving
 * an item.
 * </p>
 * 
 * <p>
 * In order to have a consistent history, for every point in time, there must
 * not be more than one object with the same natural key whose
 * {@link #validFrom} - {@link #validTо} date range covers that point. (But this
 * is currently <b>not</b> checked by this class!) It is, however, allowed that
 * for a certain point in time, there is no object at all whose validity time
 * range is covering that point.
 * </p>
 * 
 * <p>
 * Changes to those objects are not made through updates of the object, but
 * rather through archiving the object and inserting a new one. We archive an
 * object by setting a non-null {@link #validTо} date and inserting a new object
 * whose {@link #validFrom} is larger to the {@link #validTo} of the former one.
 * Hence, if we wanted to change the price of the Espresso in the example above
 * from 1,30 to 1,40, we change the {@link #validTо} date (assume that today we
 * have the 1st of December 2013) and insert a new item:
 * </p>
 * 
 * <pre>
 * Espresso: From 01.01.2010 00:00 - 31.05.2010 23:59:59.9999: 1,10 EUR
 * Espresso: From 01.06.2010 00:00 - 31.05.2011 23:59:59.9999: 1,20 EUR
 * Espresso: From 01.06.2011 00:00 - 30.11.2013 23:59:59.9999: 1,30 EUR
 * Espresso: From 01.12.2013 00:00 - null (== current): 1,40 EUR
 * </pre>
 * 
 * <p>
 * For {@link #equals(Object)} and {@link #hashCode()}, only the field
 * {@link #validFrom} is used, as it is immutable and two instances do not have
 * the same {@link #validFrom} date (see next paragraph). Via
 * {@link #additionalEqualsForSubclasses(Object)} and
 * {@link #additionalHashcodeForSubclasses()}, subclasses can add values that
 * can be considered in {@link #equals(Object)} and {@link #hashCode()}.
 * </p>
 * <p>
 * Subclasses should make sure, however, that their choice of values does not
 * break the contract of {@link #hashCode()}, which, in short is:
 * <ul>
 * <li>the hash code of an object should not change as long as the properties
 * considered in equals do not change</li>
 * <li>two instances that are equal have to produce the same hash code, but two
 * instances that have the same hash code need not be equal</li>
 * </ul>
 * </p>
 * <p>
 * Best practice: only include immutable fields for the hash code calculation,
 * and let them be a subset of the fields used for equality comparison.
 * </p>
 * 
 * <p>
 * For all objects of a given type, there should not be more than one object
 * with {@link #validFrom} set to <code>null</code>, and there should not be
 * more than one object with {@link #validTо} set to null, and it there should
 * be an ordering of all objects o1, ..., on such that holds
 * </p>
 * 
 * <ul>
 * <li>
 * <code>o<sub>1</sub>.validFrom == null || o<sub>1</sub>.validFrom &le; o<sub>1</sub>.validTo</code>
 * </li>
 * <li><code>o<sub>1</sub>.validTo &lt; o<sub>2</sub>.validFrom &le;
 * o<sub>2</sub>.validTo</code></li>
 * <li><code>o<sub>2</sub>.validTo &lt; o<sub>3</sub>.validFrom &le;
 * o<sub>3</sub>.validTo</code></li>
 * <li>...</li>
 * <li>
 * <code>o<sub>n-1</sub>.validTo &lt; o<sub>n</sub>.validFrom</code></li>
 * <li>
 * <code>o<sub>n</sub>.validFrom &le; o<sub>n</sub>.validTo || o<sub>n</sub>.validTo == null</code>
 * </li>
 * </ul>
 * 
 * <p>
 * If n==1, it is allowed that both {@link #validFrom} and {@link #validTo} are
 * set to null.
 * </p>
 * 
 * @param <SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE>
 *          The class where
 *          {@link #additionalEqualsForSubclasses(AbstractPersistentObjectWithContinuance)}
 *          is implemented. Avoids an unnecessary cast in the implementation of
 *          that method, as we already check here that the other object that is
 *          compared to this one has exactly the same type.
 * 
 * @author streit
 */
@MappedSuperclass
public abstract class AbstractPersistentObjectWithContinuance<SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE extends AbstractPersistentObjectWithContinuance<SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE>>
    extends AbstractPersistentObject {

  /**
   * The validFrom date is nullable, but only one item can have the value null,
   * the oldest one.
   */
  @NaturalId
  @Column(nullable = true)
  private Date validFrom;

  @Column(nullable = true)
  private Date validTo;


  /** Only to be used by hibernate! */
  protected AbstractPersistentObjectWithContinuance() {
  }

  public AbstractPersistentObjectWithContinuance(Date validFrom, Date validTo) {
    super();
    this.validFrom = validFrom;
    this.validTo = validTo;
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    if (this.validTo != null) {
      throw new IllegalStateException(
          "It is not allowed to overwrite an existing, non-null validTo date!");
    }
    this.validTo = validTo;
  }


  @Override
  public final boolean equals(Object obj) {
    if (obj == null || hasDifferentType(obj)) {
      return false;
    }

    // From here on, we know that the obj has exactly the same type as this
    // object!
    @SuppressWarnings("unchecked")
    final SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE castedObj = (SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE) obj;

    return performEquals(castedObj);
  }

  private boolean hasDifferentType(Object obj) {
    return !getClass().equals(obj.getClass());
  }

  private boolean performEquals(SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE obj) {
    final EqualsBuilder equalsBuilder = new EqualsBuilder();

    final AbstractPersistentObjectWithContinuance<?> objSuper = obj;
    equalsBuilder.append(validFrom, objSuper.validFrom);

    additionalEqualsForSubclasses(equalsBuilder, obj);

    return equalsBuilder.build();
  }

  /**
   * <p>
   * To avoid that in subclasses the equals method is overridden without taking
   * the validFrom field into account, we finalise the equals method and force
   * subclasses to implement the additionalEquals method to remind them to
   * provide the equality for the <b>additional</b> fields they introduce.
   * </p>
   * 
   * <p>
   * Example: if we have a subclass A that introduces a field "x" which is part
   * of the natural key of the class and hence should be used in
   * {@link #equals(Object)}, instances of A have the fields "id", "validFrom",
   * "validTo" and "x". A would then be required to compare the equality of "x"
   * in additionalEquals().
   * </p>
   * 
   * <p>
   * In case a class does not introduce any additional fields which should be
   * compared, they simply implement an empty
   * {@link #additionalEqualsForSubclasses(EqualsBuilder)} method, but that case
   * should be avoided as all objects should have a natural key that can be used
   * in {@link #equals(Object)}, and only comparing validTo is probably not
   * sufficient.
   * </p>
   * 
   * @param equalsBuilder
   *          Subclasses should add values from fields which are part of the
   *          natural key to this builder.
   * @param otherObject
   *          the other object to compare with
   */
  protected abstract void additionalEqualsForSubclasses(EqualsBuilder equalsBuilder,
      SUB_TYPE_IMPL_ADD_EQUALS_HASHCODE otherObject);

  @Override
  public final int hashCode() {
    // no NOT consider validTo, as this might change over time!
    final HashCodeBuilder builder = new HashCodeBuilder();

    builder.append(validFrom);
    additionalHashcodeForSubclasses(builder);

    return builder.toHashCode();
  }

  /**
   * Same principle as in {@link #additionalEqualsForSubclasses(Object)}. If
   * there are no fields whole hash code should be considered, just leave the
   * implementation of this method empty.
   * 
   * @param builder
   *          Subclasses should add the fields which are part of the hash code
   *          to this builder.
   */
  protected abstract void additionalHashcodeForSubclasses(HashCodeBuilder builder);

}
