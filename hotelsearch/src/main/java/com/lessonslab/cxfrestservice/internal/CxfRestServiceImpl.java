package com.lessonslab.cxfrestservice.internal;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.lessonslab.cxfrestservice.CxfRestService;
import com.lessonslab.cxfrestservice.dao.EmployeeDao;

public class CxfRestServiceImpl implements CxfRestService {
	@Autowired
	private EmployeeDao employeeDao;

	public Response getEmployeeDetail(String employeeId) {
		if (employeeId == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		// return Response.ok().build();
		return Response.ok(employeeDao.getEmployeeDetails(employeeId)).build();
	}
}