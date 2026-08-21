package com.petcare.petcare.biz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.petcare.petcare.biz.mapper.BizMapper;
import com.petcare.petcare.biz.vo.BusinessVO;
import com.petcare.petcare.file.service.FileService;
import com.petcare.petcare.hospital.mapper.HospitalMapper;
import com.petcare.petcare.member.mapper.MemberMapper;
import com.petcare.petcare.stay.mapper.StayMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BizService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BizMapper bizMapper;
    @Autowired
    private HospitalMapper hospitalMapper;
    @Autowired
    private StayMapper stayMapper;
    @Autowired
    private FileService fileService;

    public List<BusinessVO> getBusinessList(String status) throws Exception {
        return bizMapper.selectBusinessList(status);
    }

    public BusinessVO getBusinessByMemberId(String memberId) throws Exception {
        return bizMapper.selectBusinessByMemberId(memberId);
    } 

    public BusinessVO getBusinessByBizId(String bizId) throws Exception {
        return bizMapper.selectBusinessByBizId(bizId);
    } 

    public Map<String, Integer> getBizStatusCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("PENDING", bizMapper.countBusinessByStatus("PENDING"));
        counts.put("APPROVED", bizMapper.countBusinessByStatus("APPROVED"));
        counts.put("REJECTED", bizMapper.countBusinessByStatus("REJECTED"));
        return counts;
    }

    @Transactional
    public void applyBusiness(BusinessVO vo,
                              MultipartFile docFile,
                              MultipartFile licenseFile) throws Exception {

        bizMapper.insertBusiness(vo);

        // 3) 파일 업로드 — 공통 FileService 사용
        if (docFile != null && !docFile.isEmpty()) {
            fileService.uploadFile(docFile, "BIZ", vo.getBizId().toString());
        }
        if (licenseFile != null && !licenseFile.isEmpty()) {
            fileService.uploadFile(licenseFile, "BIZ", vo.getBizId().toString());
        }
    } 
    
    @Transactional
    public void bizApprove(String memberId, String action) throws Exception {
        boolean insertBiz = "APPROVED".equals(action);

        BusinessVO biz = bizMapper.selectBusinessByMemberId(memberId);
        if (biz != null && !"APPROVED".equals(biz.getStatus())){
            bizMapper.updateBusinessStatus(biz.getBizId(), action);

            if (insertBiz) {
                if ("HOSPITAL".equals(biz.getType())) {
                    hospitalMapper.insertHospital(biz.getBizId());
                }
                else if ("STAY".equals(biz.getType())) {
                    stayMapper.insertStay(biz.getBizId());
                }

                memberMapper.updateMemberRole(memberId, "BIZ");
            }
        }
    }
}
