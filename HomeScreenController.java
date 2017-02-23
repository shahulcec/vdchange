package com.src.feedthepoor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.src.feedthepoor.model.GeneralInfo;
import com.src.feedthepoor.model.Organization;
import com.src.feedthepoor.model.UserProjects;
import com.src.feedthepoor.service.OrganizationServiceImpl;
import com.src.feedthepoor.util.EncryptDecrypt;
import com.src.feedthepoor.util.FeedThePoorConstants;

@Controller
@RequestMapping("/views")
public class HomeScreenController {

	private static final Logger logger = Logger.getLogger(HomeScreenController.class);
	
	@Autowired
	@Qualifier("organizationServiceImpl")
	public OrganizationServiceImpl organizationServiceImpl;
	
	
	/**
	 * This method is used to get the unique city
	 * @param city
	 * @param place
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/{city}", method=RequestMethod.GET)
	public void getUniqueCity(@PathVariable String city, ModelMap model){
		List<String> li = new ArrayList<String>();
		try {
			logger.info("city: "+city);
			li = organizationServiceImpl.getCity(city);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("uniqueCities", li);
	}
	
	/**
	 * This method is used to get the unique place
	 * @param city
	 * @param place
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/place/{city}", method=RequestMethod.GET)
	public void getUniquePlace(@PathVariable String city, ModelMap model){
		List<String> liPlace = new ArrayList<String>();
		try {
			logger.info("city: "+city);
			liPlace = organizationServiceImpl.getPlace(city);
			logger.info("liPlace ... "+liPlace.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("uniquePlaces", liPlace);
	}
	
	@RequestMapping(value="/home/srch/{city}/{place}")
	public String handleRequest(@PathVariable String city, @PathVariable String place, ModelMap model){
		logger.info("In GET method: ");
		logger.info("city: "+city);
		logger.info("place: "+place);
		List<Organization> li = new ArrayList<Organization>();
		List<UserProjects> liUserProject = new ArrayList<UserProjects>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizations(city, place);
			liUserProject = organizationServiceImpl.getProjectsForFeed();
			
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("organization", li);
		model.addAttribute("cityName", city);
		model.addAttribute("placeName", place);
		model.addAttribute("userProjects", liUserProject);
		return FeedThePoorConstants.HOME_SCREEN;
	}
	
	@RequestMapping(value="/home/srch/flt/{filterVolume}/{city}/{place}")
	public String handleFilterVolume(@PathVariable String city, @PathVariable String place, @PathVariable String filterVolume, ModelMap model){
		logger.info("city ... "+city);
		logger.info("place ... "+place);
		logger.info("filterVolume ... "+filterVolume);
		List<Organization> li = new ArrayList<Organization>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizationsByFilter(city, place, filterVolume);
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("organization", li);
		model.addAttribute("cityName", city);
		model.addAttribute("placeName", place);
		model.addAttribute("filterVolume", filterVolume);
		return FeedThePoorConstants.HOME_SCREEN;
		
	}
	
	@RequestMapping(value="/home/srch")
	public String getOrganizations(ModelMap model, HttpServletRequest request, HttpServletResponse response){
		logger.info("In GET method: ");
		List<Organization> li = new ArrayList<Organization>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizations(null, null);
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
			//Fetch the information
			HttpSession session = request.getSession();
			List<GeneralInfo> liGeneralInfo = organizationServiceImpl.getGeneralInfo("Open", null, null, null, null, null);
			if(liGeneralInfo != null){
				logger.info("liGeneralInfo size: "+liGeneralInfo.size());
				model.addObject("generalInfo", liGeneralInfo);
				session.setAttribute("generalInfo", liGeneralInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("organization", li);
		return FeedThePoorConstants.HOME_SCREEN;
	}
	
	@RequestMapping(value="/home/srch/{city}")
	public String handleRequestWithCity(@PathVariable String city, ModelMap model){
		logger.info("In GET method: ");
		logger.info("city: "+city);
		List<Organization> li = new ArrayList<Organization>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizations(city, null);
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("organization", li);
		model.addAttribute("cityName", city);
		return FeedThePoorConstants.HOME_SCREEN;
	}
	
	@RequestMapping(value="/home/srch/flt/{filterVolume}/{city}")
	public String handleFilterVolumeWithCity(@PathVariable String city, @PathVariable String filterVolume, ModelMap model){
		logger.info("city ... "+city);
		logger.info("filterVolume ... "+filterVolume);
		List<Organization> li = new ArrayList<Organization>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizationsByFilter(city, null, filterVolume);
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("organization", li);
		model.addAttribute("cityName", city);
		model.addAttribute("filterVolume", filterVolume);
		return FeedThePoorConstants.HOME_SCREEN;
		
	}
	
	@RequestMapping(value="/home/srch/flt/{filterVolume}")
	public String handleFilterVolumeDefault(@PathVariable String filterVolume, ModelMap model){
		logger.info("filterVolume ... "+filterVolume);
		List<Organization> li = new ArrayList<Organization>();
		try{
			EncryptDecrypt td= new EncryptDecrypt();
			li = organizationServiceImpl.getOrganizationsByFilter(null, null, filterVolume);
			if(li != null){
				for(int i=0;i<li.size(); i++){
					logger.info("setting the encrypted value -------- ");
					li.get(i).setEncryptedOrgId(td.encrypt(String.valueOf(li.get(i).getOrgId())));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("organization", li);
		model.addAttribute("filterVolume", filterVolume);
		return FeedThePoorConstants.HOME_SCREEN;
		
	}
}
