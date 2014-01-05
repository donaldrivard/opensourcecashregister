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
package de.bstreit.java.oscr.business.storage;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import de.bstreit.java.oscr.business.offers.ProductOffer;
import de.bstreit.java.oscr.business.offers.dao.IProductOfferRepository;
import de.bstreit.java.oscr.business.products.Product;
import de.bstreit.java.oscr.business.products.dao.IProductRepository;

@Named
public class StorageService {

	@Inject
	private IProductRepository prodRepository;

	@Inject
	private IProductOfferRepository offerRepository;


	@Transactional
	public void saveSomeProductsAndOffers() {
		prodRepository.save(Products.ESPRESSO);
		offerRepository.save(Arrays.asList(ProductOffers.ESPRESSO, ProductOffers.CAPPUCCINO,
				ProductOffers.LATTE_MACCHIATO));
	}

	public List<Product> getProducts() {
		return Lists.newArrayList(prodRepository.findAll());
	}

	public List<ProductOffer> getOffers() {
		return Lists.newArrayList(offerRepository.findAll());
	}

}
