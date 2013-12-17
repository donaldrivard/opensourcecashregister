/*
 * Open Source Cash Register
 * 
 * Copyright (C) 2013 Bernhard Streit
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
 * See /licenses/gpl-3.txt for a copy of the GNU GPL.
 * See /README.txt for more information about the software and the author(s).
 * 
 */
package de.bstreit.java.oscr.offers;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.bstreit.java.oscr.base.finance.money.Money;
import de.bstreit.java.oscr.products.Product;

@Entity
@DiscriminatorValue(value = "ProductOffer")
public class ProductOffer extends AbstractOffer<Product> {


	@SuppressWarnings("unused")
	private ProductOffer() {
		this(null, null, null, null);
	}

	public ProductOffer(Product product, Money price, Date validFrom, Date validТо) {
		super(product, price, validFrom, validТо);
	}


}
