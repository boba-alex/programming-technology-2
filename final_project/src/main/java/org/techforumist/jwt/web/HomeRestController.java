package org.techforumist.jwt.web;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.techforumist.jwt.domain.Catalog;
import org.techforumist.jwt.domain.comment.InstructionComment;
import org.techforumist.jwt.domain.comment.StepComment;
import org.techforumist.jwt.domain.step.Product;
import org.techforumist.jwt.domain.step.ProductBlock;
import org.techforumist.jwt.domain.user.AppUser;
import org.techforumist.jwt.repository.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * All web services in this controller will be available for all the users
 *
 * @author Sarath Muraleedharan
 *
 */
@RestController
public class HomeRestController {
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private CatalogRepository catalogRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductBlockRepository productBlockRepository;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
			throw new RuntimeException("Username already exist");
		}
		List<String> roles = new ArrayList<>();
		List<Catalog> catalogs = new ArrayList<>();
		Catalog catalog = new Catalog("Amazing catalog from " + appUser.getUsername());
		catalog.setCreatorName(appUser.getUsername());
		catalogs.add(catalog);
		roles.add("USER");

		appUser.setRoles(roles);
		appUser.setCatalog(catalogs);
		return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
	}

	@RequestMapping("/user")
	public AppUser user(Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedUsername = auth.getName();
		return appUserRepository.findOneByUsername(loggedUsername);
	}


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password,
			HttpServletResponse response) throws IOException {
		String token = null;
		AppUser appUser = appUserRepository.findOneByUsername(username);
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		if (appUser != null && appUser.getPassword().equals(password)) {
			token = Jwts.builder().setSubject(username).claim("roles", appUser.getRoles()).setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
			tokenMap.put("token", token);
			tokenMap.put("user", appUser);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
		} else {
			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/view-thread/{id}", method = RequestMethod.POST)
	public ResponseEntity<StepComment> createInstructionComment(@PathVariable Long id, @RequestBody InstructionComment instructionComment) {
		Catalog catalog = catalogRepository.findOne(id);
		System.out.println("here");
		System.out.println("id: " + id);
		System.out.println("Creator: " + instructionComment.getCreatorName());
		System.out.println("Creator: " + instructionComment.getText());
		System.out.println(instructionComment.getCreatorName().equals(""));

		if(!instructionComment.getCreatorName().equals("")){
			catalog.getInstructionComments().add(instructionComment);
			catalogRepository.save(catalog);
		}
		return null;
	}

	@RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
	public ResponseEntity<StepComment> createStepComment(@PathVariable Long id, @RequestBody StepComment stepComment) {
		Product product = productRepository.findOne(id);
		System.out.println("here2");
		System.out.println("id: " + id);
		System.out.println("Creator: " + stepComment.getCreatorName());
		System.out.println("Creator: " + stepComment.getText());
		System.out.println(stepComment.getCreatorName().equals(""));

		if(!stepComment.getCreatorName().equals("")){
			product.getStepComments().add(stepComment);
			productRepository.save(product);
		}
		return null;
	}


	@RequestMapping(value = "/catalogs", method = RequestMethod.GET)
	public List<Catalog> users() {
		return catalogRepository.findAll();
	}

	@RequestMapping(value = "/view-thread/{id}", method = RequestMethod.PUT)
	public void createStep(@PathVariable Long id, @RequestBody Product product) {
		Catalog catalog = catalogRepository.findOne(id);
		if(product.getCreatorName() != null) {
			if (product.getCreatorName().equals(catalog.getCreatorName())) {
				product.setInstructionId(catalog.getId());
				catalog.getProducts().add(product);
				catalogRepository.save(catalog);
			} else {
				AppUser appUser = appUserRepository.findOneByUsername(product.getCreatorName());
				if (appUser.getRoles().contains("ADMIN")) {
					product.setInstructionId(catalog.getId());
					catalog.getProducts().add(product);
					catalogRepository.save(catalog);
				}
			}
		}
	}

	@RequestMapping(value = "/view-thread/{id}/{productID}/{userID}", method = RequestMethod.DELETE)
	public void deleteStep(@PathVariable Long id, @PathVariable Long stepID, @PathVariable Long userID) {
		Product product = productRepository.findOne(stepID);
		Catalog catalog = catalogRepository.findOne(id);
		AppUser appUser = appUserRepository.findOne(userID);
		if(catalog.getCreatorName().equals(appUser.getUsername())){
			List<Product> products = catalog.getProducts();
			products.remove(product);
			catalog.setProducts(products);
			catalogRepository.save(catalog);
			productRepository.delete(product);
			return;
		} else if(appUser.getRoles().contains("ADMIN")){
			List<Product> products = catalog.getProducts();
			products.remove(product);
			catalog.setProducts(products);
			catalogRepository.save(catalog);
			productRepository.delete(product);;
		}
	}

	@RequestMapping(value = "/product/{id}/{userID}", method = RequestMethod.PUT)
	public void createBlock(@PathVariable Long id, @PathVariable Long userID, @RequestBody ProductBlock productBlock) {
		Product product = productRepository.findOne(id);
		AppUser appUser = appUserRepository.findOne(userID);
		if (product.getCreatorName().equals(appUser.getUsername())){
			product.getProductBlocks().add(productBlock);
			productRepository.save(product);
			return;
		} else if (appUser.getRoles().contains("ADMIN")){
			product.getProductBlocks().add(productBlock);
			productRepository.save(product);
		}
	}

	@RequestMapping(value = "/product/{productID}/{productBlockID}/{userID}", method = RequestMethod.DELETE)
	public void deleteStepBlock(@PathVariable Long stepID, @PathVariable Long stepBlockID, @PathVariable Long userID) {
		ProductBlock productBlock = productBlockRepository.findOne(stepBlockID);
		Product product = productRepository.findOne(stepID);
		AppUser appUser = appUserRepository.findOne(userID);
		if(product.getCreatorName().equals(appUser.getUsername())){
			List<ProductBlock> productBlocks = product.getProductBlocks();
			productBlocks.remove(productBlock);
			product.setProductBlocks(productBlocks);
			productRepository.save(product);
			productBlockRepository.delete(productBlock);
			return;
		} else if(appUser.getRoles().contains("ADMIN")){
			List<ProductBlock> productBlocks = product.getProductBlocks();
			productBlocks.remove(productBlock);
			product.setProductBlocks(productBlocks);
			productRepository.save(product);
			productBlockRepository.delete(productBlock);
		}
	}

	@RequestMapping(value = "/view-thread/{id}", method = RequestMethod.GET)
	public Catalog userss(@PathVariable Long id) {
		return catalogRepository.findOne(id);
	}

	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
	public Product step(@PathVariable String id) {
		Catalog catalog = catalogRepository.findOne(Long.parseLong(id.split(" ")[0]));
		return catalog.getProducts().get(Integer.parseInt(id.split(" ")[1])-1);
	}

	@RequestMapping(value = "/view-profile/{id}", method = RequestMethod.GET)
	public AppUser profile(@PathVariable Long id) {
		System.out.println("view-profile");
		return appUserRepository.findOne(id);
	}


	@RequestMapping(value = "/test{id}", method = RequestMethod.GET)
	public Catalog test(@PathVariable String id)
	{
		System.out.println(" " + id);
		return null;
	}
}
