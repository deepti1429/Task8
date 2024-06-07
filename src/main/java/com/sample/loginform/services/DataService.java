package com.sample.loginform.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.loginform.Entity.Data;
import com.sample.loginform.Repository.DataRepository;

@Service
public class DataService {
    @Autowired
    private DataRepository dataRepository;

    public List<Data> getAllData() {
        return dataRepository.findAll();
    }
    public Data saveDetails(Data data) {
    	return dataRepository.save(data);
    }
    public Optional<Data>getRecordByid(Long id){
		return dataRepository.findById(id);
	}
	public void deleteRecordById(long id) {
		dataRepository.deleteById(id);
    }

   
    public Data updateRecord(long id, Data recordDetails) {
        return dataRepository.findById(id)
                   .map(record -> {
                       record.setName(recordDetails.getName());
                       record.setValue(recordDetails.getValue());
                       return dataRepository.save(record);
                   })
                   .orElseGet(() -> {
                       recordDetails.setId(id);
                       return dataRepository.save(recordDetails);
                   });
    }

}