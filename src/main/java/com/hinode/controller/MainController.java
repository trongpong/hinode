package com.hinode.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hinode.entity.*;
import com.hinode.service.*;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hinode.dto.HouseSearchCondition;
import java.util.Date;

@Controller
public class MainController {

	private final int MAX_INT = 999999999;
	private final int LIST_SIZE = 9;
	private final int LIST_ADMIN_SIZE = 10;

	@Autowired
	private HouseService houseService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private ProductServcie productServcie;

	@Autowired
	private NewsService newsService;

	@GetMapping({ "/", "/index" })
	public String init(Map<String, Object> model) {

		// Get top 6 new house
		List<House> houseList = houseService.findTopNewHouse();
		
		Map<Integer, String> imgMap = new HashMap<>();
		for (House house : houseList) {
			List<Image> imgList = houseService.findAllImageByHouseId(house.getId());
			if (!imgList.isEmpty()) {
				imgMap.put(house.getId(), Base64.getEncoder().encodeToString(imgList.get(0).getImageData()));
			}
		}
		
		model.put("houseList", houseService.findTopNewHouse());
		model.put("condition", new HouseSearchCondition());
		model.put("imgMap", imgMap);
		
		// Get slider
		List<Image> imageSliderList;
		Map<Integer, String> imgMapSlider = new HashMap<>();
		
		imageSliderList = imageService.findSlider();
		
		for (Image img : imageSliderList) {
			imgMapSlider.put(img.getId(), Base64.getEncoder().encodeToString(img.getImageData()));
		}
		
		model.put("imageSliderList", imageSliderList);
		model.put("imageSilderMap",imgMapSlider);
		
		// Get Staff
		model.put("staffList", staffService.getAll());
		
		// :: Client
		model.put("clientList", clientService.getAll());
		
		return "public/index";
	}

	@RequestMapping("/listings")
	public String list(Model model, @ModelAttribute HouseSearchCondition condition, @RequestParam(defaultValue = "0") int page) {

		// Pre search
		if (condition.getAreaTo() == 0) {
			condition.setAreaTo(MAX_INT);
		}

		if (condition.getRentFeeTo() == 0) {
			condition.setRentFeeTo(MAX_INT);
		}

		if (condition.getDepositeFeeTo() == 0) {
			condition.setDepositeFeeTo(MAX_INT);
		}

		if (condition.getGuaranteeFeeTo() == 0) {
			condition.setGuaranteeFeeTo(MAX_INT);
		}

		Page<House> houseList = houseService.findByConditionPagination(condition, page, LIST_SIZE, new Sort(Sort.Direction.ASC, "id"));
		Map<Integer, String> imgMap = new HashMap<>();
		for (House house : houseList) {
			List<Image> imgList = houseService.findAllImageByHouseId(house.getId());
			if (!imgList.isEmpty()) {
				imgMap.put(house.getId(), Base64.getEncoder().encodeToString(imgList.get(0).getImageData()));
			}
		}
		
		model.addAttribute("houseList", houseList);
		model.addAttribute("condition", condition);
		model.addAttribute("currentPage", page);
		model.addAttribute("imgMap", imgMap);
		return "public/listings";
	}

	@GetMapping("/single")
	public String single(Map<String, Object> model, @RequestParam int id) {
		// Put house
		model.put("house", houseService.getById(id));
		
		List<Image> imgList = houseService.findAllImageByHouseId(id);
		
		List<String> imgStrData = new ArrayList<>();
		for(Image img : imgList) {
			imgStrData.add(Base64.getEncoder().encodeToString(img.getImageData()));
		}
		
		model.put("imgList", imgStrData);
		return "public/single-listings";
	}

	@GetMapping("/hinodeAdmin")
	public String admin(Model model, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("houseList",
				houseService.findAllPagination(page, LIST_ADMIN_SIZE, new Sort(Sort.Direction.DESC, "id")));
		model.addAttribute("house", new House());
		model.addAttribute("currentPage", page);
		return "admin/index";
	}

	@GetMapping("/slider")
	public String slider(Model model) {
		// :: IMAGE SLIDER
		List<Image> imageSliderList;
		Map<Integer, String> imgMap = new HashMap<>();
		
		imageSliderList = imageService.findSlider();
		
		for (Image img : imageSliderList) {
			imgMap.put(img.getId(), Base64.getEncoder().encodeToString(img.getImageData()));
		}
		
		model.addAttribute("imageSliderList", imageSliderList);
		model.addAttribute("imageSilderMap",imgMap);
		
		return "admin/slider";
	}
	
	
	@GetMapping("/staff")
	public String staff(Model model) {	
		// :: STAFF
		Staff staff = new Staff();
		model.addAttribute("staff", staff);
		model.addAttribute("staffList", staffService.getAll());
		
		return "admin/staff";
	}
	
	@GetMapping("/client")
	public String client(Model model) {	
		// :: Client
		model.addAttribute("clientList", clientService.getAll());
		
		return "admin/client";
	}

	@PostMapping("/save")
	public String save(HttpServletRequest request) throws Exception {

		@SuppressWarnings("unused")
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yy/MM/dd");
		House house = new House();
		Class<?> c = house.getClass();
		List<Image> imgList = new ArrayList<>();

		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterStream = upload.getItemIterator(request);
		while (iterStream.hasNext()) {
			FileItemStream item = iterStream.next();
			InputStream stream = item.openStream();
			if (!item.isFormField()) {
				byte[] data = null;
				final ByteArrayOutputStream serializedData = new ByteArrayOutputStream();
	            int b = stream.read();
	            while (b != -1) {
	                serializedData.write(b);
	                b = stream.read();
	            }
	            data = serializedData.toByteArray();
	            if (data.length > 0) {
		            Image img = new Image();
		            img.setImageData(data);
		            imgList.add(img);
	            }
	            
			} else {
				String name = item.getFieldName();
				String value = Streams.asString(stream);
				Field field = c.getDeclaredField(name);

				if (!StringUtils.isEmpty(value) && field.getType().getName().equals("java.time.LocalDate")) {
					LocalDate date = LocalDate.parse(value, format);
					field.setAccessible(true);
					field.set(house, date);
				} 
				else if (field.getType().getName().equals("int")) {
					field.setAccessible(true);
					field.setInt(house, Integer.valueOf(value));
				}
				else if (field.getType().getName().equals("double")) {
					field.setAccessible(true);
					field.setDouble(house, Double.valueOf(value));
				}
				else {
					field.setAccessible(true);
					field.set(house, value);
				}
			}
			
            stream.close();
		}
		
		house = houseService.add(house);
		
		for (Image img : imgList) {
			img.setHouseId(house.getId());
			houseService.addImg(img);
		}
		
		return "redirect:/admin";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		houseService.delete(id);
		return "redirect:/admin";
	}
	
	@PostMapping("/saveSlider")
	public String saveSlider(HttpServletRequest request) throws Exception {
		List<Image> imgList = new ArrayList<>();
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterStream = upload.getItemIterator(request);
		while (iterStream.hasNext()) {
			FileItemStream item = iterStream.next();
			InputStream stream = item.openStream();
			if (!item.isFormField()) {
				byte[] data = null;
				final ByteArrayOutputStream serializedData = new ByteArrayOutputStream();
	            int b = stream.read();
	            while (b != -1) {
	                serializedData.write(b);
	                b = stream.read();
	            }
	            data = serializedData.toByteArray();
	            if (data.length > 0) {
		            Image img = new Image();
		            img.setImageData(data);
		            imgList.add(img);
	            }
			}
			stream.close();
		}
		for (Image img : imgList) {
			img.setHouseId(0);
			houseService.addImg(img);
		}
		return "redirect:/slider";
	}
	
	@GetMapping("/deleteSlider/{id}")
	public String deleteSlider(@PathVariable int id) {
		imageService.delete(id);;
		return "redirect:/slider";
	}
	
	@PostMapping("/saveStaff")
	public String saveStaff(HttpServletRequest request) throws Exception {
		Map<String, Object> formMap = new HashMap<String, Object>();
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterStream = upload.getItemIterator(request);
		Staff staff = new Staff();
		
		while (iterStream.hasNext()) {
			FileItemStream item = iterStream.next();
			InputStream stream = item.openStream();
			if (!item.isFormField()) {
				byte[] data = null;
				final ByteArrayOutputStream serializedData = new ByteArrayOutputStream();
	            int b = stream.read();
	            while (b != -1) {
	                serializedData.write(b);
	                b = stream.read();
	            }
	            data = serializedData.toByteArray();
	            if (data.length > 0) {
	            	formMap.put("sImage", Base64.getEncoder().encodeToString(data));
	            }
			} else {
				String name = item.getFieldName();
				String value = Streams.asString(stream);
				
				formMap.put(name, value);
			}
			
			stream.close();
		}
		
		int id = Integer.valueOf(formMap.get("id").toString());
		if (id == 0) {
			// :: New
			if (!(formMap.get("sName") == null)) {
				staff.setSName(formMap.get("sName").toString());
			}
			if (!(formMap.get("sPosition") == null)) {
				staff.setSPosition(formMap.get("sPosition").toString());
			}
			if (!(formMap.get("sImage") == null)) {
				staff.setSImage(formMap.get("sImage").toString());
			}
			staffService.add(staff);
		} else {
			// :: Update
			staff = staffService.getOne(id);
			if (!(formMap.get("sName") == null)) {
				staff.setSName(formMap.get("sName").toString());
			}
			if (!(formMap.get("sPosition") == null)) {
				staff.setSPosition(formMap.get("sPosition").toString());
			}
			if (!(formMap.get("sImage") == null)) {
				staff.setSImage(formMap.get("sImage").toString());
			}
			staffService.add(staff);
		}
		
		return "redirect:/staff";
	}
	
	@GetMapping("/deleteStaff/{id}")
	public String deleteStaff(@PathVariable int id) {
		staffService.delete(id);
		return "redirect:/staff";
	}
	
	@PostMapping("/saveClient")
	public String saveClient(Client client) {
		clientService.add(client);
		return "redirect:/client";
	}
	
	@GetMapping("/deleteClient/{id}")
	public String deleteClient(@PathVariable int id) {
		clientService.delete(id);
		return "redirect:/client";
	}
	
	@GetMapping("/wifi")
	public String wifi() {
		
		return "public/wifi";
	}

	@GetMapping("/product")
	public String product(Map<String, Object> model) {
		// Get slider
		List<Image> imageSliderList;
		Map<Integer, String> imgMapSlider = new HashMap<>();

		imageSliderList = imageService.findSlider();

		for (Image img : imageSliderList) {
			imgMapSlider.put(img.getId(), Base64.getEncoder().encodeToString(img.getImageData()));
		}

		model.put("imageSliderList", imageSliderList);
		model.put("imageSilderMap",imgMapSlider);


		model.put("productList",productList());
		return "public/product";
	}

	@GetMapping("/new")
	public String news(Map<String, Object> model) {
		// Get slider
		List<Image> imageSliderList;
		Map<Integer, String> imgMapSlider = new HashMap<>();

		imageSliderList = imageService.findSlider();

		for (Image img : imageSliderList) {
			imgMapSlider.put(img.getId(), Base64.getEncoder().encodeToString(img.getImageData()));
		}

		model.put("imageSliderList", imageSliderList);
		model.put("imageSilderMap",imgMapSlider);
		return "public/new";
	}

	@GetMapping("/productForm")
	public String ProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		model.addAttribute("productList", productList());
		return "admin/productform";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(HttpServletRequest request) throws Exception {
		Map<String, Object> formMap = new HashMap<String, Object>();
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterStream = upload.getItemIterator(request);
		Product product = new Product();

		while (iterStream.hasNext()) {
			FileItemStream item = iterStream.next();
			InputStream stream = item.openStream();
			if (!item.isFormField()) {
				byte[] data = null;
				final ByteArrayOutputStream serializedData = new ByteArrayOutputStream();
				int b = stream.read();
				while (b != -1) {
					serializedData.write(b);
					b = stream.read();
				}
				data = serializedData.toByteArray();
				if (data.length > 0) {
					formMap.put("images", Base64.getEncoder().encodeToString(data));
				}
			} else {
				String name = item.getFieldName();
				String value = Streams.asString(stream);

				formMap.put(name, value);
			}

			stream.close();
		}

		int id = Integer.valueOf(formMap.get("id").toString());
		if (id == 0) {
			// :: New
			if (!(formMap.get("productName") == null)) {
				product.setProductName(formMap.get("productName").toString());
			}
			if (!(formMap.get("price") == null)) {
				product.setPrice(formMap.get("price").toString());
			}
			if (!(formMap.get("decscription") == null)) {
				product.setDecscription(formMap.get("decscription").toString());
			}
			if (!(formMap.get("images") == null)) {
				product.setImages(formMap.get("images").toString());
			}
			product.setCreatedAt(new Date());
			product.setUpdatedAt(new Date());
			productServcie.add(product);
		} else {
			// :: Update
			product = productServcie.getById(id);
			if (!(formMap.get("productName") == null)) {
				product.setProductName(formMap.get("productName").toString());
			}
			if (!(formMap.get("price") == null)) {
				product.setPrice(formMap.get("price").toString());
			}
			if (!(formMap.get("decscription") == null)) {
				product.setDecscription(formMap.get("decscription").toString());
			}
			if (!(formMap.get("images") == null)) {
				product.setImages(formMap.get("images").toString());
			}
			product.setUpdatedAt(new Date());
			productServcie.add(product);
		}

		return "redirect:/productForm";
	}
	@GetMapping("/delete/product/{id}")
	public String deleteProduct(@PathVariable int id) {
		productServcie.delete(id);
		return "redirect:/productForm";
	}

	@GetMapping("/product-detail")
	public String productDetail(Map<String, Object> model, @RequestParam int id) {
		List<Product> productList = productServcie.getProductUpdatedAt();

		model.put("productNew", productList.subList(0,5));
		model.put("product", productServcie.getById(id));
		return "public/product-detail";
	}

	private List<Product> productList(){
		List<Product> productList = productServcie.getAllProduct();
		return productList;
	}
}
