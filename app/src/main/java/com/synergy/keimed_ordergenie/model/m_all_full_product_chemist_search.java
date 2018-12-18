package com.synergy.keimed_ordergenie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by 1132 on 12-09-2016.
 */
public class m_all_full_product_chemist_search extends BaseObservable {


    public void setProduct_ID(Integer product_ID) {
        Product_ID = product_ID;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public void setProduct_Desc(String product_Desc) {
        Product_Desc = product_Desc;
    }

    public void setPacksize(String packsize) {
        Packsize = packsize;
    }

    public void setMRP(Float MRP) {
        this.MRP = MRP;
    }

    public void setPTR(Float PTR) {
        this.PTR = PTR;
    }

    public void setManufacturer_Name(String manufacturer_Name) {
        Manufacturer_Name = manufacturer_Name;
    }

    public void setMainImagePath(String mainImagePath) {
        MainImagePath = mainImagePath;
    }

    public void setProductFormImage(String productFormImage) {
        ProductFormImage = productFormImage;
    }

    public void setRecordcount(Integer recordcount) {
        this.recordcount = recordcount;
    }

    Integer heading_type;
    Integer Product_ID;
    String Product_Code;
    String Product_Desc;
    String  Packsize;
    Float MRP;
    Float PTR;
    String Manufacturer_Name;
    String MainImagePath;
    String ProductFormImage;
    Integer recordcount;

    String OGMolecule_id;
    String Molecule_ID;
    String Molecule_Code;
    String Molecule_Desc;
    int isMolecule;

    public Integer getHeading_type() {
        return heading_type;
    }

    public void setHeading_type(Integer heading_type) {
        this.heading_type = heading_type;
    }

    public String getOGMolecule_id() {
        return OGMolecule_id;
    }

    public void setOGMolecule_id(String OGMolecule_id) {
        this.OGMolecule_id = OGMolecule_id;
    }

    public String getMolecule_ID() {
        return Molecule_ID;
    }

    public void setMolecule_ID(String molecule_ID) {
        Molecule_ID = molecule_ID;
    }

    public String getMolecule_Code() {
        return Molecule_Code;
    }

    public void setMolecule_Code(String molecule_Code) {
        Molecule_Code = molecule_Code;
    }

    public String getMolecule_Desc() {
        return Molecule_Desc;
    }

    public void setMolecule_Desc(String molecule_Desc) {
        Molecule_Desc = molecule_Desc;
    }

    public int getIsMolecule() {
        return isMolecule;
    }

    public void setIsMolecule(int isMolecule) {
        this.isMolecule = isMolecule;
    }



    @Bindable
    public Integer getProduct_ID() {
        return Product_ID;
    }

    @Bindable
    public String getProduct_Code() {
        return Product_Code;
    }

    @Bindable
    public String getProduct_Desc() {
        return Product_Desc;
    }

    @Bindable
    public String getPacksize() {
        return Packsize;
    }

    @Bindable
    public Float getMRP() {
        return MRP;
    }

    @Bindable
    public Float getPTR() {
        return PTR;
    }

    @Bindable
    public String getManufacturer_Name() {
        return Manufacturer_Name;
    }

    @Bindable
    public String getMainImagePath() {
        return MainImagePath;
    }

    @Bindable
    public String getProductFormImage() {
        return ProductFormImage;
    }

    @Bindable
    public Integer getRecordcount() {
        return recordcount;
    }









}
