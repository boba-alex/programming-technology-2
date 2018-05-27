package org.techforumist.jwt.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.techforumist.jwt.domain.Catalog;
import org.techforumist.jwt.domain.step.Product;
import org.techforumist.jwt.domain.step.ProductBlock;
import org.techforumist.jwt.domain.user.AppUser;
import org.techforumist.jwt.domain.comment.InstructionComment;
import org.techforumist.jwt.domain.comment.StepComment;
import org.techforumist.jwt.repository.AppUserRepository;
import org.techforumist.jwt.repository.CatalogRepository;
import org.techforumist.jwt.repository.ProductRepository;

/**
 * Rest controller for authentication and user details. All the web services of
 * this rest controller will be only accessible for ADMIN users only
 * 
 * @author Sarath Muraleedharan
 *
 */
@RestController
@RequestMapping(value = "/api")
public class AppUserRestController {
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private CatalogRepository catalogRepository;

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Web service for getting all the appUsers in the application.
	 * 
	 * @return list of all AppUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<AppUser> users() {
		return appUserRepository.findAll();
	}

	/**
	 * Web service for getting a user by his ID
	 * 
	 * @param id
	 *            appUser ID
	 * @return appUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<AppUser> userById(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}
	}

	/**
	 * Method for deleting a user by his ID
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedUsername = auth.getName();
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
			throw new RuntimeException("You cannot delete your account");
		} else {
			appUserRepository.delete(appUser);
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}

	}

	/**
	 * Method for adding a appUser
	 * 
	 * @param appUser
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
			throw new RuntimeException("Username already exist");
		}
		return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
	}

	/**
	 * Method for editing an user details
	 * 
	 * @param appUser
	 * @return modified appUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public AppUser updateUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null
				&& appUserRepository.findOneByUsername(appUser.getUsername()).getId() != appUser.getId()) {
			throw new RuntimeException("Username already exist");
		}
		return appUserRepository.save(appUser);
	}

	@PreAuthorize("hasRole('ROLE_CATALOG_MANAGER')")
	@RequestMapping(value = "/create-catalog", method = RequestMethod.GET)
	public List<AppUser> users2() {
		return appUserRepository.findAll();
	}

	@RequestMapping(value = "/create-catalog", method = RequestMethod.PUT)
	public void createCatalog(@RequestBody Catalog catalog) {

		System.out.println(catalog.getName() + " " + catalog.getCategory() +
				" " + catalog.getTags());

		AppUser appUser = appUserRepository.findOneByUsername(catalog.getCreatorName());
		System.out.println(appUser);
		if (appUser == null ){
			System.out.println("null");
			return;
		}

		appUser.getCatalog().add(catalog);
		appUserRepository.save(appUser);

		addProduct(appUser.getCatalog().get(appUser.getCatalog().size()-1));
	}

	private void addProduct(Catalog catalog){
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setInstructionId(catalog.getId());
		product.setName("First super product 1");
		product.setCreatorName(catalog.getCreatorName());

		Product product1 = new Product();
		product1.setInstructionId(catalog.getId());
		product1.setName("Small product 1");
		product1.setCreatorName(catalog.getCreatorName());

		Product product2 = new Product();
		product2.setInstructionId(catalog.getId());
		product2.setName("Very big and bla bla " +
				"The term initialism uses a similar method, but the " +
				"word is pronounced letter by letterFirst super product 1");
		product2.setCreatorName(catalog.getCreatorName());

		products.add(product);
		products.add(product1);
		products.add(product2);
		catalog.setProducts(products);

		catalogRepository.save(catalog);
		addStepComments(catalog.getProducts().get(catalog.getProducts().size()-1));
		addProductBlocks(catalog.getProducts().get(catalog.getProducts().size()-1));
		addInstructionComments(catalog);
	}

	private void addInstructionComments(Catalog catalog){
		List<InstructionComment> instructionComments = new ArrayList<>();

		InstructionComment instructionComment = new InstructionComment();
		instructionComment.setInstructionId(catalog.getId());
		instructionComment.setText("All men must die !!");
		instructionComment.setCreatorName(catalog.getCreatorName());

		InstructionComment instructionComment1 = new InstructionComment();
		instructionComment1.setInstructionId(catalog.getId());
		instructionComment1.setText("text !!");
		instructionComment1.setCreatorName(catalog.getCreatorName());

		InstructionComment instructionComment2 = new InstructionComment();
		instructionComment2.setInstructionId(catalog.getId());
		instructionComment2.setText("Please note: An acronym (from Greek: " +
				"-acro = sharp, pointed; -onym = name) in its pure form denotes" +
				" a combination of letters (usually from an abbreviation) ");
		instructionComment2.setCreatorName(catalog.getCreatorName());

		instructionComments.add(instructionComment);
		instructionComments.add(instructionComment1);
		instructionComments.add(instructionComment2);
		catalog.setInstructionComments(instructionComments);

		catalogRepository.save(catalog);
	}

	private void addProductBlocks(Product product){

		List<ProductBlock> productBlocks = new ArrayList<>();

		ProductBlock productBlock = new ProductBlock();
		productBlock.setType("text");
		productBlock.setText("Hamsters are rodents belonging to the subfamily Cricetinae." +
				" The subfamily contains about 25 species, classified in six or seven" +
				" genera. They have become established as popular small house pets," +
				" and, partly because they are easy to breed in captivity, hamsters are " +
				"often used as laboratory animals.\n" +
				"\n" +
				"In the wild, hamsters are crepuscular and remain underground during the " +
				"day to avoid being caught by predators. They feed primarily on seeds," +
				" fruits, and vegetation, and will occasionally eat burrowing insects." +
				" They have elongated cheek pouches extending to their shoulders in " +
				"which they carry food back to their burrows.");

		ProductBlock productBlock1 = new ProductBlock();
		productBlock1.setType("image");
		productBlock1.setImageLink("http://res.cloudinary.com/demo/image/upload/front_face.png");

		ProductBlock productBlock2 = new ProductBlock();
		productBlock2.setType("video");
		productBlock2.setVideoLink("video link ");

		productBlocks.add(productBlock);
		productBlocks.add(productBlock1);
		productBlocks.add(productBlock2);
		product.setProductBlocks(productBlocks);

		productRepository.save(product);
	}


	private void addStepComments(Product product){

		List<StepComment> stepComments = new ArrayList<>();

		StepComment stepComment = new StepComment();
		stepComment.setStepId(product.getId());
		stepComment.setText("You need to rest as well as work");
		stepComment.setCreatorName(product.getCreatorName());

		StepComment stepComment1 = new StepComment();
		stepComment1.setStepId(product.getId());
		stepComment1.setText("You");
		stepComment1.setCreatorName(product.getCreatorName());

		StepComment stepComment2 = new StepComment();
		stepComment2.setStepId(product.getId());
		stepComment2.setText("To use caps for the acronyms themselves " +
				"is generally not considered poor netiquette;");
		stepComment2.setCreatorName(product.getCreatorName());

		stepComments.add(stepComment);
		stepComments.add(stepComment1);
		stepComments.add(stepComment2);
		product.setStepComments(stepComments);

		productRepository.save(product);
	}

}