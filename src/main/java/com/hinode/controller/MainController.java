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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hinode.dto.HouseSearchCondition;
import com.hinode.entity.House;
import com.hinode.entity.Image;
import com.hinode.service.HouseService;
import com.hinode.service.ImageService;

@Controller
public class MainController {

	private final int MAX_INT = 999999999;
	private final int LIST_SIZE = 9;
	private final int LIST_ADMIN_SIZE = 10;

	@Autowired
	private HouseService houseService;
	
	@Autowired
	private ImageService imageService;

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

	@GetMapping("/admin")
	public String admin(Model model, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("houseList",
				houseService.findAllPagination(page, LIST_ADMIN_SIZE, new Sort(Sort.Direction.DESC, "id")));
		model.addAttribute("house", new House());
		model.addAttribute("currentPage", page);
		return "admin/index";
	}

	@GetMapping("/page")
	public String page(Model model) {
		List<Image> imageSliderList;
		Map<Integer, String> imgMap = new HashMap<>();
		
		imageSliderList = imageService.findSlider();
		
		for (Image img : imageSliderList) {
			imgMap.put(img.getId(), Base64.getEncoder().encodeToString(img.getImageData()));
		}
		
		model.addAttribute("imageSliderList", imageSliderList);
		model.addAttribute("imageSilderMap",imgMap);
		return "admin/pages";
	}

	@PostMapping("/save")
	public String save(HttpServletRequest request) throws Exception {

		@SuppressWarnings("unused")
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
		}
		for (Image img : imgList) {
			img.setHouseId(0);
			houseService.addImg(img);
		}
		return "redirect:/page";
	}
	
	@GetMapping("/deleteSlider/{id}")
	public String deleteSlider(@PathVariable int id) {
		imageService.delete(id);;
		return "redirect:/page";
	}
}
