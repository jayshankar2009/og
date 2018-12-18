package com.synergy.keimed_ordergenie.Constants;

import com.synergy.keimed_ordergenie.database.ChemistCart;
import com.synergy.keimed_ordergenie.database.StockistProducts;
import com.synergy.keimed_ordergenie.model.m_callplans;
import com.synergy.keimed_ordergenie.model.m_delivery_invoice_list;
import com.synergy.keimed_ordergenie.model.m_pendingbills;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    //public static String isAddedProductsInSQL = "0";
    public static ArrayList<StockistProducts> posts;
    public static int ableToSelectInvoice = 0;
    public static int enteredAmount = 0;
    public static int balanceAmount = 0;
    public static List<m_pendingbills> selectedInvoice;
    public static String currentDate;
    public static List <m_delivery_invoice_list>selectedDeliveryInvoice;
   // List<List<ChemistCart>>
    public static List <m_callplans> selectedChemistCartItemList;
    public static List<List<ChemistCart>> selectedChemistCart;
    public static int balanceAmountDelivery = 0;
}
