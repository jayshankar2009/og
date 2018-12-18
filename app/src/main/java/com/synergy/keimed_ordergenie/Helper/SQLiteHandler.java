package com.synergy.keimed_ordergenie.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.synergy.keimed_ordergenie.Constants.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1132 on 27-09-2016.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "ordergenie";
    public SQLiteDatabase mydb;

    //ordergenie STOCKIST WISE producttable

    private static final String TABLE_STOCKIST_WISE_PRODUCTS = "stockist_wise_products";
    // STOCKIST WISE producttable Columns names
    private static final String F_KEY_SP_ID = "serial_no";
    private static final String F_KEY_SP_ITEMCODE = "Itemcode";
    private static final String F_KEY_SP_STOCKIST_ID = "Stockist_id";
    private static final String F_KEY_SP_PRODUCT_ID = "Product_ID";
    private static final String F_KEY_SP_ITEMNAME = "Itemname";
    private static final String F_KEY_SP_PACKSIZE = "Packsize";
    private static final String F_KEY_SP_MRP = "MRP";
    private static final String F_KEY_SP_RATE = "Rate";
    private static final String F_KEY_SP_STOCK = "Stock";
    private static final String F_KEY_SP_TYPE = "type";
    private static final String F_KEY_SP_MFGCODE = "MfgCode";
    private static final String F_KEY_SP_MFGNAME = "MfgName";
    private static final String F_KEY_SP_DOSEFORM = "DoseForm";
    private static final String F_KEY_SP_SCHEME = "Scheme";
    /* Chemist List For Create Order */
    private static final String ChemistListForCreateOrderSalesmanTable = "ChemistListForCreateOrderSalesman";
    private static final String Key_Id_ChemistListForCreateOrderSalesman = "Id";
    private static final String Key_UserId_ChemistListForCreateOrderSalesman = "UserId";
    private static final String Key_ChemistId_ChemistListForCreateOrderSalesman = "ChemistId";
    private static final String Key_CustCode_ChemistListForCreateOrderSalesman = "CustomerCode";
    private static final String Key_CustName_ChemistListForCreateOrderSalesman = "CustomerName";
    private static final String Key_Email_ChemistListForCreateOrderSalesman = "Email";
    private static final String Key_Mobile_ChemistListForCreateOrderSalesman = "Mobile";
    private static final String Key_Location_ChemistListForCreateOrderSalesman = "Location";
    private static final String Key_Latitude_ChemistListForCreateOrderSalesman = "Latitude";
    private static final String Key_Longitude_ChemistListForCreateOrderSalesman = "Longitude";
    private static final String Key_OutstandingBill_ChemistListForCreateOrderSalesman = "OutstandingBill";
    private static final String Key_EXPIRY_DATE="DLExpiry";
    private static final String Key_EXPIRY_ID="DLExpInd";


    /* Save Chemist List for Salesman Offline */
    private static final String SalesmanChemistListTable = "SalesmanChemistList";
    private static final String Key_Id_SalesmanChemistList = "Id";
    private static final String Key_UserId_SalesmanChemistList = "UserId";
    private static final String Key_ChemistId_SalesmanChemistList = "ChemistId";
    private static final String Key_StockistCallPlanID_SalesmanChemistList = "StockistCallPlanID";
    private static final String Key_DayNumber_SalesmanChemistList = "DayNumber";
    private static final String Key_ChemistName_SalesmanChemistList = "ChemistName";
    private static final String Key_Latitude_SalesmanChemistList = "Latitude";
    private static final String Key_Longitude_SalesmanChemistList = "Longitude";
    private static final String Key_Address_SalesmanChemistList = "Address";
    private static final String Key_ProfileImage_SalesmanChemistList = "ProfileImage";
    private static final String Key_InTime_SalesmanChemistList = "InTime";
    private static final String Key_OutTime_SalesmanChemistList = "OutTime";
    private static final String Key_Status_SalesmanChemistList = "Status";
    private static final String Key_BillAmount_SalesmanChemistList = "BillAmount";
    private static final String Key_Sequence_SalesmanChemistList = "Sequence";
    private static final String Key_TaskStatus_SalesmanChemistList = "TaskStatus";
    private static final String Key_OrderRecevied_SalesmanChemistList = "OrderRecevied";
    private static final String Key_PassedDate_SalesmanChemistList = "PassedDate";
    private static final String Key_isLocked_SalesmanChemistList = "isLocked";
    private static final String Key_DLExpiry_SalesmanChemistList = "DLExpiry";
    private static final String KEY_CHEMIST_LOCATION_FLAG ="locationFlag";
    private static final String Key_Block_Reason_SalesmanChemistList = "Block_Reason";


    /* Chemist StockistWise Product */
    private static final String ChemistStockistWiseProduct = "ChemistStockistWiseProduct";
    // STOCKIST WISE producttable Columns names
    private static final String KEY_ID_CHEMIST_PRO = "Id";
    private static final String KEY_ITEMCODE_CHEMIST_PRO = "ItemCode";
    private static final String KEY_STOCKIST_ID_CHEMIST_PRO = "StockistId";
    private static final String KEY_PRODUCT_ID_CHEMIST_PRO = "ProductId";
    private static final String KEY_ITEMNAME_CHEMIST_PRO = "ItemName";
    private static final String KEY_PACKSIZE_CHEMIST_PRO = "PackSize";
    private static final String KEY_MRP_CHEMIST_PRO = "MRP";
    private static final String KEY_RATE_CHEMIST_PRO = "Rate";
    private static final String KEY_STOCK_CHEMIST_PRO = "Stock";
    private static final String KEY_TYPE_CHEMIST_PRO = "type";
    private static final String KEY_MFGCODE_CHEMIST_PRO = "MfgCode";
    private static final String KEY_MFGNAME_CHEMIST_PRO = "MfgName";
    private static final String KEY_DOSEFORM_CHEMIST_PRO = "DoseForm";
    private static final String KEY_SCHEME_CHEMIST_PRO = "Scheme";

    // ordergenie Salesman products producttable

    private static final String TABLE_SALESMAN_PRODUCTS = "salesman_products";
    // STOCKIST WISE producttable Columns names
    private static final String F_KEY_SS_ID = "serial_no";
    private static final String F_KEY_SS_ITEMCODE = "Itemcode";
    private static final String F_KEY_SS_STOCKIST_ID = "Stockist_id";
    private static final String F_KEY_SS_PRODUCT_ID = "Product_ID";//F_KEY_SS_PRODUCT_ID
    private static final String F_KEY_SS_ITEMNAME = "Itemname";
    private static final String F_KEY_SS_PACKSIZE = "Packsize";
    private static final String F_KEY_SS_MRP = "MRP";
    private static final String F_KEY_SS_RATE = "Rate";
    private static final String F_KEY_SS_STOCK = "Stock";
    private static final String F_KEY_SS_TYPE = "type";
    private static final String F_KEY_SS_MFGCODE = "MfgCode";
    private static final String F_KEY_SS_MFGNAME = "MfgName";
    private static final String F_KEY_SS_DOSEFORM = "DoseForm";
    private static final String F_KEY_SS_SCHEME = "Scheme";
    private static final String key_product_halfscheme = "HalfScheme";
    private static final String key_product_percentscheme = "PercentScheme";
    private static final String key_product_legendname = "LegendName";
    private static final String key_product_legendcolor = "LegendColor";
    private static final String key_product_BoxSize = "BoxSize";
    private static final String key_product_minQ = "minQ";
    private static final String key_product_maxQ = "maxQ";
    //private static final String F_KEY_SS_LEGENDDATA = "LegendData";
    //chemist order table master
    private static final String TABLE_LEGEND_MASTER = "LEGEND_MASTER";
    // chemist order table Columns names
    private static final String F_KEY_LM_ID = "id";
    private static final String F_KEY_LM_STOCKIST_ID = "stockistId";
    private static final String F_KEY_LM_LEGENDNAME = "LegendName";
    private static final String F_KEY_LM_COLOR_CODE = "ColorCode";
    private static final String F_KEY_LM_START_RANGE = "StartRange";
    private static final String F_KEY_LM_END_RANGE = "EndRange";
    private static final String F_KEY_LM_STOCKLEGENDID = "StockLegendID";
    private static final String F_KEY_LM_LEGENDMODE = "LegendMode";


    //chemist order table master
    private static final String TABLE_CHEMIST_ORDER = "chemist_order";
    // chemist order table Columns names
    private static final String F_KEY_COR_ID = "serial_no";
    private static final String F_KEY_COR_DOC_NO = "DOC_NO";
    private static final String F_KEY_COR_DOC_DATE = "Doc_Date";
    private static final String F_KEY_COR_STOCKIST_ID = "Stockist_Client_id";
    private static final String F_KEY_COR_ITEMS = "Items";
    private static final String F_KEY_COR_REMARK = "Remark";
    private static final String F_KEY_COR_ORDER_AMOUNT = "Amount";
    private static final String F_KEY_COR_ORDER_STATUS = "Status";
    private static final String F_KEY_COR_CREATEDON = "Createdon";
    private static final String F_KEY_COR_CREATEBY = "CreatedBy";


    //chemist cart table
    private static final String TABLE_CHEMIST_CART = "chemist_cart";
    // chemist cart table Columns names
    private static final String F_KEY_CC_ID = "serial_no";
    private static final String F_KEY_CC_DOC_NO = "DOC_NO";
    private static final String F_KEY_CC_STOCKISTID = "Stockist_Client_id";
    private static final String F_KEY_CC_ITEM_COUNT = "Items";
    private static final String F_KEY_CC_ORDER_AMOUNT = "Amount";
    private static final String F_KEY_CC_REMARK = "Remarks";
    private static final String F_KEY_CC_ORDER_PLACED = "order_placed";
    private static final String F_KEY_CC_ORDER_SYNC = "order_sync";
    private static final String F_KEY_CC_DOC_DATE = "Doc_Date";
    private static final String F_KEY_CC_STATUS = "status";
    private static final String F_KEY_CC_CREATED_ON = "Createdon";


    //chemist cart DETAIL table master
    private static final String TABLE_CHEMIST_CART_DETAIL = "chemist_cart_detail";
    // chemist order DETAIL table Columns names
    private static final String F_KEY_CORD_ID = "serial_no";
    private static final String F_KEY_CORD_DOC_NO = "DOC_NO";
    private static final String F_KEY_CORD_DOC_ITEM_NO = "Doc_item_No";
    private static final String F_KEY_CORD_PRODUCT_ID = "Product_ID";
    private static final String F_KEY_CORD_QTY = "Qty";
    private static final String F_KEY_CORD_UOM = "UOM";
    private static final String F_KEY_CORD_RATE = "Rate";
    private static final String F_KEY_CORD_PRICE = "Price";
    private static final String F_KEY_CORD_MRP = "MRP";
    private static final String F_KEY_CORD_CREATED_ON = "Createdon";


    //CallPlan tabel
    private static final String TABLE_CALL_PLAN = "call_plan";
    // CallPlan table Columns names
    private static final String F_KEY_CP_ID = "serial_no";
    private static final String F_KEY_CP_CALL_PLAN_ID = "call_plan_id";
    private static final String F_KEY_CP_CLIENT_ID = "client_id";
    private static final String F_KEY_CP_LOCATION = "location";
    private static final String F_KEY_CP_CALL_START = "call_start";
    private static final String F_KEY_CP_CALL_END = "call_end";
    private static final String F_KEY_CP_CALL_DURATION = "call_duration";
    private static final String F_KEY_CP_DELIVERY = "delivery";
    private static final String F_KEY_CP_PAYMENT = "payment";
    private static final String F_KEY_CP_RETURN = "return";
    private static final String F_KEY_CP_ORDER = "order_place";


    //CallPlan tabel
    private static final String TABLE_CALL_PLAN_DETAIL = "call_plan_detail";
    // CallPlan table Columns names
    private static final String F_KEY_CPD_ID = "serial_no";
    private static final String F_KEY_CPD_CALL_PLAN_ID = "call_plan_id";
    private static final String F_KEY_CPD_TASK_ID = "task_id";
    private static final String F_KEY_CPD_TASK_NAME = "task_name";
    private static final String F_KEY_CPD_TASK_STATUS = "task_status";
    private static final String F_KEY_CPD_SELFIE = "selfie_image";


    //Chemist order detail table

    private static final String TABLE_CHEMIST_ORDER_DEATILS = "chemist_order_detail";
    // order detail table table Columns names
    private static final String F_KEY_COD_ID = "serial_no";
    private static final String F_KEY_COD_ORDER_NO = "OrderNo";
    private static final String F_KEY_COD_ITEMSR_NO_ = "ItemSrNo";
    private static final String F_KEY_COD_ITEM_NAME = "Item_Name";
    private static final String F_KEY_COD_MRP = "MRP";
    private static final String F_KEY_COD_PACK_SIZE = "Packsize";
    private static final String F_KEY_COD_QTY = "Qty";


    //Chemist order sync
    private static final String TABLE_CHEMIST_ORDER_SYNC = "chemist_order_sync";
    // Chemist order sync table Columns names

    private static final String F_KEY_COS_ID = "serial_no";
    private static final String F_KEY_COS_DOC_ID = "DOC_ID";
    private static final String F_KEY_COS_JSON = "json";
    private static final String F_KEY_COS_SYNCED = "synced";




    /* Offline Save Orders */
    /*private static final String TablePlacedOrders = "PlacedOrders";
    private static final String Key_Id_PlacedOrder = "Id";
    private static final String Key_UserId_PlacedOrder = "UserId";
    private static final String Key_CreatedBy_PlacedOrder = "CreatedBy";
    private static final String Key_ClientId_PlacedOrder = "ClientId";
    private static final String Key_DocNo_PlacedOrder = "DocNo";
    private static final String Key_DocDate_PlacedOrder = "DocDate";
    private static final String Key_StockistClientId_PlacedOrder = "StockistClientId";
    private static final String Key_Remarks_PlacedOrder = "Remarks";
    private static final String Key_Items_PlacedOrder = "UserId";
    private static final String Key_Amount_PlacedOrder = "UserId";
    private static final String Key_Status_PlacedOrder = "UserId";
    private static final String Key_CreatedOn_PlacedOrder = "UserId";
    private static final String Key_CartIdAvailable_PlacedOrder = "UserId";
    private static final String Key_TransactionNo_PlacedOrder = "UserId";
    private static final String Key_DocItemNo_PlacedOrder = "UserId";
    private static final String Key_ProductId_PlacedOrder = "UserId";
    private static final String Key_Quantity_PlacedOrder = "UserId";
    private static final String Key_UOM_PlacedOrder = "UserId";
    private static final String Key_Rate_PlacedOrder = "UserId";
    private static final String Key_Price_PlacedOrder = "UserId";
    private static final String Key_MRP_PlacedOrder = "UserId";*/


    /* Offline Save Orders */
    private static final String TablePlacedOrders = "PlacedOrders";
    private static final String Key_Id_PlacedOrder = "Id";
    private static final String Key_UserId_PlacedOrder = "UserId";
    private static final String Key_CreatedBy_PlacedOrder = "CreatedBy";
    private static final String Key_ClientId_PlacedOrder = "ClientId";
    private static final String Key_ChemistId_PlacedOrder = "ChemistId";
    private static final String Key_ChemistName_PlacedOrder = "ChemistName";
    private static final String Key_DocNo_PlacedOrder = "DocNo";
    private static final String Key_DocDate_PlacedOrder = "DocDate";
    private static final String Key_Remarks_PlacedOrder = "Remarks";
    private static final String Key_Items_PlacedOrder = "Items";
    private static final String Key_Amount_PlacedOrder = "Amount";
    private static final String Key_Status_PlacedOrder = "Status";
    private static final String Key_CreatedOn_PlacedOrder = "CreatedOn";
    private static final String Key_DeliveryOption_PlacedOrder = "DeliveryOption";
    private static final String Key_ItemsJson_PlacedOrder = "ItemsJson";
    private static final String Key_ItemCodes_PlacedOrder = "ItemCodes";


    /* Save Invoice List For Delivery */
    private static final String TableInvoiceListDelivery = "InvoiceListDelivery";
    private static final String Key_Id_InvoiceListDelivery = "Id";
    private static final String Key_UserId_InvoiceListDelivery = "UserId";
    private static final String Key_StockistId_InvoiceListDelivery = "StockistId";
    private static final String Key_ChemistId_InvoiceListDelivery = "ChemistId";
    private static final String Key_OrderNo_InvoiceListDelivery = "OrderNo";
    private static final String Key_ChemistName_InvoiceListDelivery = "ChemistName";
    private static final String Key_OrderStatus_InvoiceListDelivery = "OrderStatus";
    private static final String Key_InvoiceDate_InvoiceListDelivery = "InvoiceDate";
    private static final String Key_InvoiceNo_InvoiceListDelivery = "InvoiceNo";
    private static final String Key_InvoiceAmount_InvoiceListDelivery = "InvoiceAmount";
    private static final String Key_BillStatus_InvoiceListDelivery = "BillStatus";
    private static final String Key_Items_InvoiceListDelivery = "Items";
    private static final String Key_OrderDate_InvoiceListDelivery = "OrderDate";
    private static final String Key_OrderAmount_InvoiceListDelivery = "OrderAmount";


    /* Add Offline Delivery Delivered/Undelivered */
    private static final String TableSaveDeliveryOffline = "SaveDeliveryOffline";
    private static final String Key_Id = "Id";
    private static final String Key_UserId = "UserId";
    private static final String Key_StartTime = "StartTime";
    private static final String Key_EndTime = "EndTime";
    private static final String Key_CustomerName = "CustomerName";
    private static final String Key_StockistClientId = "StockistClientId";
    private static final String Key_ChemistId = "ChemistId";
    private static final String Key_CallPlanId = "CallPlanId";
    private static final String Key_TransactionNo = "TransactionNo";
    private static final String Key_InvoiceNo = "InvoiceNo";
    private static final String Key_OrderDate = "OrderDate";
    private static final String Key_ItemCount = "ItemCount";
    private static final String Key_InvoiceAmount = "InvoiceAmount";
    private static final String Key_Status = "Status";
    private static final String Key_Description = "Description";
    private static final String Key_OrderImg = "OrderImg";
    private static final String Key_SignImg = "SignImg";
    private static final String Key_SavedTime = "SavedTime";
    private static final String Key_CurrentLocLat = "LocationLat";
    private static final String Key_CurrentLocLong = "LocationLong";
    private static final String Key_NoOfPackets = "NoOfPackets";
    private static final String Key_NoOfCases = "NoOfCases";


    /* Invoices List Payment */
    private static final String TableInvoiceListPayment = "InvoicesList";
    private static final String Key_Id_InvoiceListPayment = "Id";
    private static final String Key_UserId_InvoiceListPayment = "UserId";
    private static final String Key_StockistId_InvoiceListPayment = "StockistId";
    private static final String Key_ChemistId_InvoiceListPayment = "ChemistId";
    private static final String Key_InvoiceNo_InvoiceListPayment = "InvoiceNo";
    private static final String Key_InvoiceId_InvoiceListPayment = "InvoiceId";
    private static final String Key_InvoiceDate_InvoiceListPayment = "InvoiceDate";
    private static final String Key_TotalItems_InvoiceListPayment = "TotalItems";
    private static final String Key_BillAmount_InvoiceListPayment = "BillAmount";
    private static final String Key_PaymentReceived_InvoiceListPayment = "PaymentReceived";
    private static final String Key_BalanceAmount_InvoiceListPayment = "BalanceAmount";
    private static final String Key_PaymentStatus_InvoiceListPayment = "PaymentStatus";
    private static final String Key_GrandTotal_InvoiceListPayment = "GrandTotal";
    private static final String Key_TotalDiscount_InvoiceListPayment = "TotalDiscount";
    private static final String Key_LedgerBalance_InvoiceListPayment = "ledgerBalance";
    private static final String Key_PaymentFlag_InvoiceListPayment = "invoice_flag";


    /* Collect Payment Salesman */
    private static final String TableCollectPaymentSalesman = "CollectPaymentSalesman";
    private static final String Key_Id_PaymentSalesman = "Id";
    private static final String Key_StockistId_PaymentSalesman = "StockistId";
    private static final String Key_UserId_PaymentSalesman = "UserId";
    private static final String Key_ChemistId_PaymentSalesman = "ChemistId";
    private static final String Key_PaymentId_PaymentSalesman = "PaymentId";
    private static final String Key_CreatedBy_PaymentSalesman = "CreatedBy";
    private static final String Key_Amount_PaymentSalesman = "Amount";
    private static final String Key_PaymentDate_PaymentSalesman = "PaymentDate";
    private static final String Key_Remarks_PaymentSalesman = "Remarks";
    private static final String Key_Status_PaymentSalesman = "Status";
    private static final String Key_CreatedDate_PaymentSalesman = "CreatedDate";
    private static final String Key_PaymentMode_PaymentSalesman = "PaymentMode";
    private static final String Key_BankName_PaymentSalesman = "BankName";
    private static final String Key_ChequeNo_PaymentSalesman = "ChequeNo";
    private static final String Key_ChequeAmount_PaymentSalesman = "ChequeAmount";
    private static final String Key_ChequeDate_PaymentSalesman = "ChequeDate";
    private static final String Key_MicrNo_PaymentSalesman = "MicrNo";
    private static final String Key_Narration_PaymentSalesman = "Narration";
    private static final String Key_Comments_PaymentSalesman = "Comments";
    private static final String Key_Flag_PaymentSalesman = "Flag";
    private static final String Key_InvoiceList_PaymentSalesman = "InvoiceList";
    private static final String Key_ChemistName_PaymentSalesman = "ChemistName";

    //private static final String Key_SelectedInvoices_PaymentSalesman = "SelectedInvoices";


    /* Collect Payment Salesman Selected Invoices */
    private static final String TableCollectPaymentSelectedInvoice = "CollectPaymentSelectedInvoice";
    private static final String Key_Id_PaymentSelectedInvoice = "Id";
    private static final String Key_StockistId_PaymentSelectedInvoice = "StockistId";
    private static final String Key_UserId_PaymentSelectedInvoice = "UserId";
    private static final String Key_ChemistId_PaymentSelectedInvoice = "ChemistId";
    private static final String Key_PaymentId_PaymentSelectedInvoice = "PaymentId";
    private static final String Key_InvoiceNo_PaymentSelectedInvoice = "InvoiceNo";
    private static final String Key_PaidAmount_PaymentSelectedInvoice = "PaidAmount";
    private static final String Key_PaymentStatus_PaymentSelectedInvoice = "PaymentStatus";
    private static final String Key_CreatedDate_PaymentSelectedInvoice = "CreatedDate";
    private static final String Key_Status_PaymentSelectedInvoice = "Status";

    /* Table  Customer List Delivery */
    private static final String TableDeliveryScheduleSalesman = "DeliveryScheduleSalesman";
    private static final String Key_Id_DeliverySchedule = "Id";//
    private static final String Key_ClientId_DeliverySchedule= "ClientID";
    private static final String Key_UserID_DeliverySchedule = "UserId";//
    private static final String Key_Client_Code_DeliverySchedule  = "Client_Code";
    private static final String Key_Client_Name_DeliverySchedule  = "Client_Name";
    private static final String Key_Client_Address_DeliverySchedule= "Client_Address";

    /* Table  Invoice List Delivery */
    private static final String TableDeliveryScheduleInvoices= "DeliveryScheduleInvoices";
    private static final String Key_Id_DeliveryScheduleInvoices = "Id";
    private static final String Key_UserId_DeliveryScheduleInvoices = "UserId";
    private static final String Key_ClientID_DeliveryScheduleInvoices = "ClientID";
    private static final String Key_ClientNAME_DeliveryScheduleInvoices = "Client_Name";
    private static final String Key_ErpsalesmanID_DeliveryScheduleInvoices = "ErpsalesmanID";
    private static final String Key_DeliveryDate_DeliveryScheduleInvoices= "DeliveryDate";
    private static final String Key_DeliveryStatus_DeliveryScheduleInvoices= "DeliveryStatus";
    private static final String Key_OrderID_DeliveryScheduleInvoices = "OrderID";
    private static final String Key_InvoiceDate_DeliveryScheduleInvoices = "InvoiceDate";
    private static final String Key_BoxCount_DeliveryScheduleInvoices= "BoxCount";
    private static final String Key_InvoiceNo_DeliveryScheduleInvoices = "InvoiceNo";
    private static final String Key_TotalAmount_DeliveryScheduleInvoices = "TotalAmount";
    private static final String Key_TotalItems_DeliveryScheduleInvoices = "TotalItems";
    private static final String Key_Package_count_DeliveryScheduleInvoices= "Package_count";
    private static final String Key_DeliveryId_DeliveryScheduleInvoices = "DeliveryId";
    private static final String Key_DeliveryFlag_DeliveryScheduleInvoices = "DeliveryFlag";
    //add new coloumn for deivery description
    private static final String Key_DeliveryDescription_DeliveryScheduleInvoices = "Description";


    /* Collect Delivery Salesman */
    private static final String TableCollectDeliverySalesman = "CollectDeliverySalesman";
    private static final String Key_Id_DeliverySalesman = "Id";
    private static final String Key_StockistId_DeliverySalesman = "StockistID";
    private static final String Key_DeliveryId_DeliverySalesman = "Delivery_doc_no";
    private static final String Key_ChemistId_DeliverySalesman = "ErpchemistID";
    private static final String Key_Package_count_DeliverySalesman = "Package_count";
    private static final String Key_DeliveryStatus_DeliverySalesman = "DeliveryStatus";
    private static final String Key_CreatedBy_DeliverySalesman = "ErpsalesmanID";
    private static final String Key_Delivery_date_DeliverySalesman = "Delivery_date";
    private static final String Key_Order_Image_DeliverySalesman = "Order_Image";
    private static final String Key_Sign_Image_DeliverySalesman = "Sign_Image";
    private static final String Key_Latitude_DeliverySalesman = "Latitude";
    private static final String Key_Longitude_DeliverySalesman = "Longitude";
    private static final String Key_Description_DeliverySalesman = "Description";
    private static final String Key_UserId_DeliverySalesman = "UserId";
    private static final String Key_Flag_DeliverySalesman = "Flag";


    /* Collect Delivery Salesman Selected Invoices */
    private static final String TableCollectDeliverySelectedInvoice = "CollectDeliverySelectedInvoice";
    private static final String Key_Id_DeliverySelectedInvoice = "Id";
    private static final String Key_StockistId_DeliverySelectedInvoice = "StockistId";
    private static final String Key_UserId_DeliverySelectedInvoice = "UserId";
    private static final String Key_ChemistId_DeliverySelectedInvoice = "ChemistId";
    private static final String Key_InvoiceNo_DeliverySelectedInvoice = "Invoice_no";
    private static final String Key_Status_DeliverySelectedInvoice = "Status";
    private static final String Key_DeliveryId_DeliverySelectedInvoice = "Delivery_doc_no";
    private static final String Key_Delivery_KeyID_DeliverySelectedInvoice = "DeliveryId";
    //  private static final String Key_Delivery_Description_DeliverySelectedInvoice = "Description";

    //Table for Location


    private static final String TableForLocation = "TableForLocation";
    private static final String Key_USERID = "user_Id";
    private static final String Key_CUSTID = "call_plan_customer_id";
    private static final String Key_TASK = "task";
    private static final String Key_Address = "address";

    private static final String Key_TRANSACTION_ID = "Tran_No";
    private static final String Key_LATITUDE = "Latitude";
    private static final String Key_LONGITUDE = "Longitude";
    private static final String Key_UNIQ_ID = "unqid";

    public static final   String TABLE_TABLE_SALESMAN_PRODUCTS = "" + TABLE_SALESMAN_PRODUCTS + "("
            + F_KEY_SS_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_SS_ITEMCODE + " TEXT,"
            + F_KEY_SS_STOCKIST_ID + " TEXT,"
            + F_KEY_SS_PRODUCT_ID + " TEXT,"
            + F_KEY_SS_ITEMNAME + " TEXT,"
            + F_KEY_SS_PACKSIZE + " TEXT,"
            + F_KEY_SS_MRP + " TEXT,"
            + F_KEY_SS_RATE + " TEXT ,"
            + F_KEY_SS_STOCK + " TEXT,"
            + F_KEY_SS_TYPE + " TEXT,"
            + F_KEY_SS_MFGCODE + " TEXT,"
            + F_KEY_SS_MFGNAME + " TEXT,"
            + F_KEY_SS_DOSEFORM + " TEXT,"
            + F_KEY_SS_SCHEME + " TEXT, "
            + key_product_halfscheme + " TEXT,"
            + key_product_percentscheme + " TEXT, "
            + key_product_legendname + " TEXT,"
            + key_product_legendcolor + " TEXT, "
            + key_product_minQ + " TEXT, "
            + key_product_maxQ + " TEXT, "
            //  + F_KEY_SS_SCHEME + " TEXT "
            + key_product_BoxSize + " TEXT "


            + ")";

    public static final   String CREATE_TABLE_TABLE_SALESMAN_PRODUCTS = "CREATE TABLE " + TABLE_SALESMAN_PRODUCTS + "("
            + F_KEY_SS_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_SS_ITEMCODE + " TEXT,"
            + F_KEY_SS_STOCKIST_ID + " TEXT,"
            + F_KEY_SS_PRODUCT_ID + " TEXT,"
            + F_KEY_SS_ITEMNAME + " TEXT,"
            + F_KEY_SS_PACKSIZE + " TEXT,"
            + F_KEY_SS_MRP + " TEXT,"
            + F_KEY_SS_RATE + " TEXT ,"
            + F_KEY_SS_STOCK + " TEXT,"
            + F_KEY_SS_TYPE + " TEXT,"
            + F_KEY_SS_MFGCODE + " TEXT,"
            + F_KEY_SS_MFGNAME + " TEXT,"
            + F_KEY_SS_DOSEFORM + " TEXT,"
            + F_KEY_SS_SCHEME + " TEXT, "
            + key_product_halfscheme + " TEXT,"
            + key_product_percentscheme + " TEXT, "
            + key_product_legendname + " TEXT,"
            + key_product_legendcolor + " TEXT, "
            + key_product_minQ + " TEXT, "
            + key_product_maxQ + " TEXT, "
            //  + F_KEY_SS_SCHEME + " TEXT "
            + key_product_BoxSize + " TEXT "

            + ")";

    private static final String CreateifNotExistTableForLocation = "" + TableForLocation + "("
            + Key_Id_InvoiceListPayment + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Key_USERID + " TEXT,"
            + Key_CUSTID + " TEXT,"
            + Key_TASK + " TEXT,"
            + Key_Address + " TEXT,"
            + F_KEY_CC_DOC_NO + " TEXT,"
            + Key_TRANSACTION_ID + " TEXT,"
            + Key_LATITUDE + " TEXT,"
            + Key_LONGITUDE + " TEXT,"
            + Key_UNIQ_ID + " TEXT" + ")";

    private static final String CreateTableForLocation = "CREATE TABLE " + TableForLocation + "("
            + Key_Id_InvoiceListPayment + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Key_USERID + " TEXT,"
            + Key_CUSTID + " TEXT,"
            + Key_TASK + " TEXT,"
            + Key_Address + " TEXT,"
            + F_KEY_CC_DOC_NO + " TEXT,"
            + Key_TRANSACTION_ID + " TEXT,"
            + Key_LATITUDE + " TEXT,"
            + Key_LONGITUDE + " TEXT,"
            + Key_UNIQ_ID + " TEXT" + ")";


    private static final String CreateTableChemistListSalesmanForCreateOrder = "CREATE TABLE " + ChemistListForCreateOrderSalesmanTable + "("
            + Key_Id_ChemistListForCreateOrderSalesman + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_UserId_ChemistListForCreateOrderSalesman
            + " TEXT," + Key_ChemistId_ChemistListForCreateOrderSalesman + " TEXT," + Key_CustCode_ChemistListForCreateOrderSalesman + " TEXT,"
            + Key_CustName_ChemistListForCreateOrderSalesman + " TEXT," + Key_Email_ChemistListForCreateOrderSalesman + " TEXT,"
            + Key_Mobile_ChemistListForCreateOrderSalesman + " TEXT," + Key_Location_ChemistListForCreateOrderSalesman + " TEXT,"
            + Key_Latitude_ChemistListForCreateOrderSalesman + " TEXT," + Key_Longitude_ChemistListForCreateOrderSalesman + " TEXT,"
            + Key_OutstandingBill_ChemistListForCreateOrderSalesman + " TEXT" + ")";

    /* Chemist List Salesman For CreateOrders */


    /* Salesman Chemist List Table */
    private static final String CreateTableSalesmanChemistList = "CREATE TABLE " + SalesmanChemistListTable + "(" + Key_Id_SalesmanChemistList +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_UserId_SalesmanChemistList + " TEXT," + Key_ChemistId_SalesmanChemistList +
            " TEXT," + Key_StockistCallPlanID_SalesmanChemistList + " TEXT," + Key_DayNumber_SalesmanChemistList + " TEXT," +
            Key_ChemistName_SalesmanChemistList + " TEXT," + Key_Latitude_SalesmanChemistList + " TEXT," + Key_Longitude_SalesmanChemistList +
            " TEXT," + Key_Address_SalesmanChemistList + " TEXT," + Key_ProfileImage_SalesmanChemistList + " TEXT," + Key_InTime_SalesmanChemistList + " TEXT," +
            Key_OutTime_SalesmanChemistList + " TEXT," + Key_Status_SalesmanChemistList + " TEXT," + Key_BillAmount_SalesmanChemistList +
            " TEXT," + Key_Sequence_SalesmanChemistList + " TEXT," + Key_TaskStatus_SalesmanChemistList + " TEXT," +
            Key_OrderRecevied_SalesmanChemistList + " TEXT,"
            + Key_PassedDate_SalesmanChemistList + " TEXT,"
            + Key_isLocked_SalesmanChemistList + " TEXT,"
            + Key_DLExpiry_SalesmanChemistList + " TEXT,"
            + KEY_CHEMIST_LOCATION_FLAG + " TEXT,"
            + Key_Block_Reason_SalesmanChemistList + " TEXT"+ ")";

    /* Save Placed Order Table */
    private static final String CreateTablePlaceOrder = "CREATE TABLE " + TablePlacedOrders + "(" + Key_Id_PlacedOrder + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Key_UserId_PlacedOrder + " TEXT," + Key_CreatedBy_PlacedOrder + " TEXT," + Key_ClientId_PlacedOrder + " TEXT," +
            Key_ChemistId_PlacedOrder + " TEXT," + Key_ChemistName_PlacedOrder + " TEXT," + Key_DocNo_PlacedOrder + " TEXT," +
            Key_DocDate_PlacedOrder + " TEXT," + Key_Remarks_PlacedOrder + " TEXT," + Key_Items_PlacedOrder + " TEXT," + Key_Amount_PlacedOrder +
            " TEXT," + Key_Status_PlacedOrder + " TEXT," + Key_CreatedOn_PlacedOrder + " TEXT," + Key_DeliveryOption_PlacedOrder + " TEXT," +
            Key_ItemsJson_PlacedOrder + " TEXT," + Key_ItemCodes_PlacedOrder + " TEXT" + ")";

    /* Invoice List Delivery */
    private static final String CreateTableInvoiceListDelivery = "CREATE TABLE " + TableInvoiceListDelivery + "(" + Key_Id_InvoiceListDelivery +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_UserId_InvoiceListDelivery + " TEXT," + Key_StockistId_InvoiceListDelivery + " TEXT," +
            Key_ChemistId_InvoiceListDelivery + " TEXT," + Key_OrderNo_InvoiceListDelivery + " TEXT," + Key_ChemistName_InvoiceListDelivery +
            " TEXT," + Key_OrderStatus_InvoiceListDelivery + " TEXT," + Key_InvoiceDate_InvoiceListDelivery + " TEXT," + Key_InvoiceNo_InvoiceListDelivery +
            " TEXT," + Key_InvoiceAmount_InvoiceListDelivery + " TEXT," + Key_BillStatus_InvoiceListDelivery + " TEXT," + Key_Items_InvoiceListDelivery +
            " TEXT," + Key_OrderDate_InvoiceListDelivery + " TEXT," + Key_OrderAmount_InvoiceListDelivery + " TEXT" + ")";


    /* Create Save Delivery Offline */
    private static final String CreateTableSaveDeliveryOffline = "CREATE TABLE " + TableSaveDeliveryOffline + "(" + Key_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Key_UserId + " TEXT," + Key_StartTime + " TEXT," + Key_EndTime + " TEXT," + Key_CustomerName + " TEXT,"
            + Key_StockistClientId + " TEXT," + Key_ChemistId + " TEXT," + Key_CallPlanId + " TEXT," + Key_TransactionNo + " TEXT,"
            + Key_InvoiceNo + " TEXT," + Key_OrderDate + " TEXT," + Key_ItemCount + " TEXT," + Key_InvoiceAmount + " TEXT,"
            + Key_Status + " TEXT," + Key_Description + " TEXT," + Key_OrderImg + " TEXT," + Key_SignImg + " TEXT," + Key_SavedTime + " TEXT,"
            + Key_CurrentLocLat + " TEXT," + Key_CurrentLocLong + " TEXT," + Key_NoOfPackets + " TEXT," + Key_NoOfCases + " TEXT" + ")";


    /* Payment Invoice List */
    //apurva
    private static final String CreateInvoiceListPayment = "CREATE TABLE " + TableInvoiceListPayment + "(" + Key_Id_InvoiceListPayment +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_UserId_InvoiceListPayment + " TEXT," + Key_StockistId_InvoiceListPayment + " TEXT," +
            Key_ChemistId_InvoiceListPayment + " TEXT," + Key_InvoiceNo_InvoiceListPayment + " TEXT," + Key_InvoiceId_InvoiceListPayment +
            " TEXT," + Key_InvoiceDate_InvoiceListPayment + " DATE," + Key_TotalItems_InvoiceListPayment + " TEXT," +
            Key_BillAmount_InvoiceListPayment + " TEXT," + Key_PaymentReceived_InvoiceListPayment + " TEXT," +
            Key_BalanceAmount_InvoiceListPayment + " TEXT," + Key_PaymentStatus_InvoiceListPayment + " TEXT," +
            Key_GrandTotal_InvoiceListPayment + " TEXT," + Key_TotalDiscount_InvoiceListPayment + " TEXT," + Key_LedgerBalance_InvoiceListPayment + " TEXT,"+Key_PaymentFlag_InvoiceListPayment + " TEXT" + ")";


    /* Delivery Schedule List */

    private static final String CreateTableDeliveryScheduleSalesman = "CREATE TABLE " + TableDeliveryScheduleSalesman + "(" + Key_Id_DeliverySchedule + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Key_ClientId_DeliverySchedule + " TEXT," + Key_UserID_DeliverySchedule + " TEXT," + Key_Client_Code_DeliverySchedule + " TEXT," + Key_Client_Name_DeliverySchedule + " TEXT," + Key_Client_Address_DeliverySchedule + " TEXT " + ")";


    private static final String CreateTableDeliveryScheduleInvoices = "CREATE TABLE " +
            TableDeliveryScheduleInvoices + "(" + Key_Id_DeliveryScheduleInvoices + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Key_ErpsalesmanID_DeliveryScheduleInvoices + " TEXT,"
            + Key_UserId_DeliveryScheduleInvoices + " TEXT,"
            + Key_ClientID_DeliveryScheduleInvoices + " TEXT,"
            + Key_DeliveryDate_DeliveryScheduleInvoices + " TEXT,"
            + Key_DeliveryStatus_DeliveryScheduleInvoices + " TEXT,"
            + Key_OrderID_DeliveryScheduleInvoices + " TEXT,"
            + Key_InvoiceDate_DeliveryScheduleInvoices + " TEXT,"
            + Key_BoxCount_DeliveryScheduleInvoices + " TEXT,"
            + Key_InvoiceNo_DeliveryScheduleInvoices + " TEXT,"
            + Key_TotalAmount_DeliveryScheduleInvoices + " TEXT,"
            +Key_TotalItems_DeliveryScheduleInvoices + " TEXT,"
            +Key_Package_count_DeliveryScheduleInvoices + " TEXT,"
            +Key_DeliveryId_DeliveryScheduleInvoices + " TEXT, "
            + Key_DeliveryFlag_DeliveryScheduleInvoices + " TEXT, "
            + Key_DeliveryDescription_DeliveryScheduleInvoices + " TEXT, "
            + Key_ClientNAME_DeliveryScheduleInvoices + " TEXT " + ")";




    private static final String CreateTablePaymentCollection = "CREATE TABLE " + TableCollectPaymentSalesman + "(" + Key_Id_PaymentSalesman
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_PaymentSalesman + " TEXT," +
            Key_UserId_PaymentSalesman + " TEXT," + Key_ChemistId_PaymentSalesman + " TEXT," + Key_PaymentId_PaymentSalesman +
            " TEXT," + Key_CreatedBy_PaymentSalesman + " TEXT," + Key_Amount_PaymentSalesman + " TEXT," +
            Key_PaymentDate_PaymentSalesman + " TEXT," + Key_Remarks_PaymentSalesman + " TEXT," + Key_Status_PaymentSalesman +
            " TEXT," + Key_CreatedDate_PaymentSalesman + " TEXT," + Key_PaymentMode_PaymentSalesman + " TEXT," +
            Key_BankName_PaymentSalesman + " TEXT," + Key_ChequeNo_PaymentSalesman + " TEXT," + Key_ChequeAmount_PaymentSalesman +
            " TEXT," + Key_ChequeDate_PaymentSalesman + " TEXT," + Key_MicrNo_PaymentSalesman + " TEXT," +
            Key_Narration_PaymentSalesman + " TEXT," + Key_Comments_PaymentSalesman + " TEXT," + Key_Flag_PaymentSalesman + " TEXT," + Key_InvoiceList_PaymentSalesman + " TEXT," + Key_ChemistName_PaymentSalesman + " TEXT " + ")";/* Key_SelectedInvoices_PaymentSalesman +
" TEXT" + ")";*/

    private static final String CreateTablePaymentSelectedInvoice = "CREATE TABLE " + TableCollectPaymentSelectedInvoice + "(" +
            Key_Id_PaymentSelectedInvoice + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_PaymentSelectedInvoice + " TEXT," +
            Key_UserId_PaymentSelectedInvoice + " TEXT," + Key_ChemistId_PaymentSelectedInvoice + " TEXT," +
            Key_PaymentId_PaymentSelectedInvoice + " TEXT," + Key_InvoiceNo_PaymentSelectedInvoice + " TEXT," +
            Key_PaidAmount_PaymentSelectedInvoice + " TEXT," + Key_PaymentStatus_PaymentSelectedInvoice + " TEXT," +
            Key_CreatedDate_PaymentSelectedInvoice + " TEXT," + Key_Status_PaymentSelectedInvoice + " TEXT" + ")";

    /*Table Offline Delivery Collection*/
     private static final String CreateTableDeliveryCollection = "CREATE TABLE " + TableCollectDeliverySalesman + "(" + Key_Id_DeliverySalesman
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_DeliverySalesman + " TEXT," +
            Key_DeliveryId_DeliverySalesman + " TEXT," + Key_ChemistId_DeliverySalesman + " TEXT," + Key_Package_count_DeliverySalesman +
            " TEXT," + Key_DeliveryStatus_DeliverySalesman + " TEXT," + Key_CreatedBy_DeliverySalesman + " TEXT," +
            Key_Delivery_date_DeliverySalesman + " TEXT," + Key_Order_Image_DeliverySalesman + " TEXT," + Key_Sign_Image_DeliverySalesman +
            " TEXT," + Key_Latitude_DeliverySalesman + " TEXT," + Key_Longitude_DeliverySalesman + " TEXT," +
            Key_Description_DeliverySalesman + " TEXT," + Key_UserId_DeliverySalesman + " TEXT," + Key_Flag_DeliverySalesman + " TEXT " + ")";



    /*Table Offline Delivery Invoice Collection*/
//    private static final String CreateTableDeliverySelectedInvoice = "CREATE TABLE " + TableCollectDeliverySelectedInvoice + "(" +
//            Key_Id_DeliverySelectedInvoice + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_DeliverySelectedInvoice + " TEXT," +
//            Key_UserId_DeliverySelectedInvoice + " TEXT," + Key_ChemistId_DeliverySelectedInvoice + " TEXT," +
//            Key_InvoiceNo_DeliverySelectedInvoice + " TEXT," + Key_Status_DeliverySelectedInvoice + " TEXT," +
//            Key_DeliveryId_DeliverySelectedInvoice + " TEXT" + ")";
    private static final String CreateTableDeliverySelectedInvoice = "CREATE TABLE " + TableCollectDeliverySelectedInvoice + "(" +
            Key_Id_DeliverySelectedInvoice + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_DeliverySelectedInvoice + " TEXT," +
            Key_UserId_DeliverySelectedInvoice + " TEXT," + Key_ChemistId_DeliverySelectedInvoice + " TEXT," +
            Key_InvoiceNo_DeliverySelectedInvoice + " TEXT," + Key_Status_DeliverySelectedInvoice + " TEXT," +
            Key_DeliveryId_DeliverySelectedInvoice + " TEXT," + Key_Delivery_KeyID_DeliverySelectedInvoice + " TEXT " + ")";

    //update coloumn for delivery description
//    private static final String CreateTableDeliverySelectedInvoice = "CREATE TABLE " + TableCollectDeliverySelectedInvoice + "(" +
//            Key_Id_DeliverySelectedInvoice + " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_StockistId_DeliverySelectedInvoice + " TEXT," +
//            Key_UserId_DeliverySelectedInvoice + " TEXT," + Key_ChemistId_DeliverySelectedInvoice + " TEXT," +
//            Key_InvoiceNo_DeliverySelectedInvoice + " TEXT," + Key_Status_DeliverySelectedInvoice + " TEXT," +
//            Key_DeliveryId_DeliverySelectedInvoice + " TEXT," + Key_Delivery_KeyID_DeliverySelectedInvoice + " TEXT," + Key_Delivery_Description_DeliverySelectedInvoice + " TEXT " + ")";
//***edited by harish


    public static final String CREATE_TABLE_STOCKIST_WISE_PRODUCTS = "CREATE TABLE " + TABLE_STOCKIST_WISE_PRODUCTS + "("
            + F_KEY_SP_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_SP_ITEMCODE + " TEXT,"
            + F_KEY_SP_STOCKIST_ID + " TEXT,"
            + F_KEY_SP_PRODUCT_ID + " TEXT,"
            + F_KEY_SP_ITEMNAME + " TEXT,"
            + F_KEY_SP_PACKSIZE + " TEXT,"
            + F_KEY_SP_MRP + " TEXT,"
            + F_KEY_SP_RATE + " TEXT ,"
            + F_KEY_SP_STOCK + " TEXT,"
            + F_KEY_SP_TYPE + " TEXT,"
            + F_KEY_SP_MFGCODE + " TEXT,"
            + F_KEY_SP_MFGNAME + " TEXT,"
            + F_KEY_SP_DOSEFORM + " TEXT,"
            + F_KEY_SP_SCHEME + " TEXT " + ")";


    public static final String CREATE_TABLE_CHEMIST_STOCKIST_PRO = "CREATE TABLE " + ChemistStockistWiseProduct + "("
            + KEY_ID_CHEMIST_PRO + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + KEY_ITEMCODE_CHEMIST_PRO + " TEXT,"
            + KEY_STOCKIST_ID_CHEMIST_PRO + " TEXT,"
            + KEY_PRODUCT_ID_CHEMIST_PRO + " TEXT,"
            + KEY_ITEMNAME_CHEMIST_PRO + " TEXT,"
            + KEY_PACKSIZE_CHEMIST_PRO + " TEXT,"
            + KEY_MRP_CHEMIST_PRO + " TEXT,"
            + KEY_RATE_CHEMIST_PRO + " TEXT ,"
            + KEY_STOCK_CHEMIST_PRO + " TEXT,"
            + KEY_TYPE_CHEMIST_PRO + " TEXT,"
            + KEY_MFGCODE_CHEMIST_PRO + " TEXT,"
            + KEY_MFGNAME_CHEMIST_PRO + " TEXT,"
            + KEY_DOSEFORM_CHEMIST_PRO + " TEXT,"
            + KEY_SCHEME_CHEMIST_PRO + " TEXT " + ")";


    public static final String CREATE_TABLE_CHEMIST_CART = "CREATE TABLE " + TABLE_CHEMIST_CART + "("
            + F_KEY_CC_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_CC_DOC_NO + " TEXT,"
            + F_KEY_CC_STOCKISTID + " TEXT,"
            + F_KEY_CC_ITEM_COUNT + " TEXT,"
            + F_KEY_CC_ORDER_AMOUNT + " TEXT,"
            + F_KEY_CC_ORDER_PLACED + " TEXT,"
            + F_KEY_CC_STATUS + " TEXT,"
            + F_KEY_CC_CREATED_ON + " TEXT,"
            + F_KEY_CC_DOC_DATE + " TEXT,"
            + F_KEY_CC_ORDER_SYNC + " TEXT,"
            + F_KEY_CC_REMARK + " TEXT " + ")";


    public static final String CREATE_TABLE_CALL_PLAN = "CREATE TABLE " + TABLE_CALL_PLAN + "("
            + F_KEY_CP_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_CP_CALL_PLAN_ID + " TEXT,"
            + F_KEY_CP_CLIENT_ID + " TEXT,"
            + F_KEY_CP_LOCATION + " TEXT,"
            + F_KEY_CP_CALL_START + " TEXT,"
            + F_KEY_CP_CALL_END + " TEXT,"
            + F_KEY_CP_CALL_DURATION + " TEXT,"
            + F_KEY_CP_DELIVERY + " TEXT,"
            + F_KEY_CP_PAYMENT + " TEXT,"
            + F_KEY_CP_ORDER + " TEXT,"
            + F_KEY_CP_RETURN + " TEXT " + ")";

    public static final String CREATE_TABLE_CALL_PLAN_DETAILS = "CREATE TABLE " + TABLE_CALL_PLAN_DETAIL + "("
            + F_KEY_CPD_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_CPD_CALL_PLAN_ID + " TEXT,"
            + F_KEY_CPD_TASK_ID + " TEXT,"
            + F_KEY_CPD_TASK_NAME + " TEXT,"
            + F_KEY_CPD_TASK_STATUS + " TEXT,"
            + F_KEY_CPD_SELFIE + " TEXT " + ")";

    public static final String CREATE_TABLE_CHEMIST_ORDER_DEATILS = "CREATE TABLE " + TABLE_CHEMIST_ORDER_DEATILS + "("
            + F_KEY_COD_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_COD_ORDER_NO + " TEXT,"
            + F_KEY_COD_ITEMSR_NO_ + " TEXT,"
            + F_KEY_COD_ITEM_NAME + " TEXT,"
            + F_KEY_COD_MRP + " TEXT,"
            + F_KEY_COD_PACK_SIZE + " TEXT,"
            + F_KEY_COD_QTY + " TEXT " + ")";

    public static final String CREATE_TABLE_CHEMIST_ORder_sync = "CREATE TABLE " + TABLE_CHEMIST_ORDER_SYNC + "("
            + F_KEY_COS_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_COS_DOC_ID + " TEXT,"
            + F_KEY_COS_JSON + " TEXT,"
            + F_KEY_COS_SYNCED + " TEXT " + ")";

    public static final String CREATE_TABLE_CHEMIST_ORDER = "CREATE TABLE " + TABLE_CHEMIST_ORDER + "("
            + F_KEY_COR_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_COR_DOC_NO + " TEXT,"
            + F_KEY_COR_DOC_DATE + " TEXT,"
            + F_KEY_COR_STOCKIST_ID + " TEXT,"
            + F_KEY_COR_ITEMS + " TEXT,"
            + F_KEY_COR_REMARK + " TEXT,"
            + F_KEY_COR_ORDER_AMOUNT + " TEXT,"
            + F_KEY_COR_ORDER_STATUS + " TEXT,"
            + F_KEY_COR_CREATEDON + " TEXT,"
            + F_KEY_COR_CREATEBY + " TEXT " + ")";

    public static final String CREATE_TABLE_LEGEND_MASTER = "CREATE TABLE " + TABLE_LEGEND_MASTER + "("
            + F_KEY_LM_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_LM_STOCKIST_ID + " TEXT,"
            + F_KEY_LM_LEGENDNAME + " TEXT,"
            + F_KEY_LM_COLOR_CODE + " TEXT,"
            + F_KEY_LM_START_RANGE + " TEXT,"
            + F_KEY_LM_END_RANGE + " TEXT,"
            + F_KEY_LM_STOCKLEGENDID + " TEXT,"
            + F_KEY_LM_LEGENDMODE + " TEXT"
            + ")";

    public static final String CREATE_TABLE_CHEMIST_ORDER_DETAIL = "CREATE TABLE " + TABLE_CHEMIST_CART_DETAIL + "("
            + F_KEY_CORD_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            + F_KEY_CORD_DOC_NO + " TEXT,"
            + F_KEY_CORD_DOC_ITEM_NO + " TEXT,"
            + F_KEY_CORD_PRODUCT_ID + " TEXT,"
            + F_KEY_CORD_QTY + " TEXT,"
            + F_KEY_CORD_UOM + " TEXT,"
            + F_KEY_CORD_RATE + " TEXT,"
            + F_KEY_CORD_PRICE + " TEXT,"
            + F_KEY_CORD_MRP + " TEXT,"
            + F_KEY_CORD_CREATED_ON + " TEXT " + ")";


    public void create_Write_db_object()
    {
        mydb = this.getWritableDatabase();
    }
    public void create_read_db_object()
    {
        mydb = this.getReadableDatabase();
    }
    //***edited by harish
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_STOCKIST_WISE_PRODUCTS);
        /* Chemist Product By Stockist */
        db.execSQL(CREATE_TABLE_CHEMIST_STOCKIST_PRO);
        //+ F_KEY_SS_LEGENDDATA + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_TABLE_SALESMAN_PRODUCTS);
        db.execSQL(CREATE_TABLE_CHEMIST_CART);
        db.execSQL(CREATE_TABLE_CALL_PLAN);
        db.execSQL(CREATE_TABLE_CALL_PLAN_DETAILS);
        db.execSQL(CREATE_TABLE_CHEMIST_ORDER_DEATILS);
        db.execSQL(CREATE_TABLE_CHEMIST_ORder_sync);
        db.execSQL(CREATE_TABLE_CHEMIST_ORDER);
        db.execSQL(CREATE_TABLE_LEGEND_MASTER);
        db.execSQL(CREATE_TABLE_CHEMIST_ORDER_DETAIL);
        db.execSQL(CreateTableChemistListSalesmanForCreateOrder);
        /* Create Chemist List Table */
        db.execSQL(CreateTableSalesmanChemistList);
        /* Create Table PlaceOrder */
        db.execSQL(CreateTablePlaceOrder);
        /* Create Invoice List Table Delivery */
        db.execSQL(CreateTableInvoiceListDelivery);
        /* Create Save Delivery Table Offline */
        db.execSQL(CreateTableSaveDeliveryOffline);
        /* Create Invoice List Table */
        db.execSQL(CreateInvoiceListPayment);
        /* Create Invoice & Single Invoice Detail Table */
        //db.execSQL(CreateTableInvoicePayment);
        /* Create Payment Collection Table */
        db.execSQL(CreateTablePaymentCollection);
        /* Create Payment Collection Selected Invoices */
        db.execSQL(CreateTablePaymentSelectedInvoice);
        db.execSQL(CreateTableDeliveryScheduleSalesman);
        db.execSQL(CreateTableDeliveryScheduleInvoices);
        db.execSQL(CreateTableDeliveryCollection);
        db.execSQL(CreateTableDeliverySelectedInvoice);
        //new added
        db.execSQL(CreateTableForLocation);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( newVersion >= 3) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKIST_WISE_PRODUCTS);
            /* Chemist Product By Stockist */
            db.execSQL("DROP TABLE IF EXISTS " +ChemistStockistWiseProduct);
            //+ F_KEY_SS_LEGENDDATA + " TEXT " + ")";
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_SALESMAN_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHEMIST_CART);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CALL_PLAN);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CALL_PLAN_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHEMIST_ORDER_DEATILS);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHEMIST_ORDER_SYNC);

            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHEMIST_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_LEGEND_MASTER);
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHEMIST_CART_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " +ChemistListForCreateOrderSalesmanTable);
            /* Create Chemist List Table */
            db.execSQL("DROP TABLE IF EXISTS " +SalesmanChemistListTable);
            /* Create Table PlaceOrder */
            db.execSQL("DROP TABLE IF EXISTS " +TablePlacedOrders);
            /* Create Invoice List Table Delivery */
            db.execSQL("DROP TABLE IF EXISTS " +TableInvoiceListDelivery);
            /* Create Save Delivery Table Offline */
            db.execSQL("DROP TABLE IF EXISTS " +TableSaveDeliveryOffline);
            /* Create Invoice List Table */
            db.execSQL("DROP TABLE IF EXISTS " +TableInvoiceListPayment);
            /* Create Invoice & Single Invoice Detail Table */
            //db.execSQL(CreateTableInvoicePayment);
            /* Create Payment Collection Table */
            db.execSQL("DROP TABLE IF EXISTS " +TableCollectPaymentSalesman);
            /* Create Payment Collection Selected Invoices */
            db.execSQL("DROP TABLE IF EXISTS " +TableCollectPaymentSelectedInvoice);
            db.execSQL("DROP TABLE IF EXISTS " +TableDeliveryScheduleSalesman);
            db.execSQL("DROP TABLE IF EXISTS " +TableDeliveryScheduleInvoices);
            db.execSQL("DROP TABLE IF EXISTS " +TableCollectDeliverySalesman);
            db.execSQL("DROP TABLE IF EXISTS " +TableCollectDeliverySelectedInvoice);
            //new added
            db.execSQL("DROP TABLE IF EXISTS " +TableForLocation);
            onCreate(db);
            //  db.execSQL("DROP INDEX idx_itemcode  ");
           // CREATE UNIQUE INDEX IF NOT EXISTS MyUniqueIndexName ON auth_user (email)
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_itemcode on " + TABLE_SALESMAN_PRODUCTS +"("+F_KEY_SS_PRODUCT_ID+")");
         //   db.execSQL(""ALTER TABLE "+ TABLE_SALESMAN_PRODUCTS + " ADD COLUMN " + key_product_BoxSize + " TEXT;");
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_TABLE_SALESMAN_PRODUCTS);

       //
            //
            if(!isColumnExists(db,TABLE_SALESMAN_PRODUCTS,key_product_BoxSize)){
                db.execSQL("ALTER TABLE salesman_products ADD COLUMN BoxSize TEXT;");
            }




            db.execSQL("CREATE TABLE IF NOT EXISTS "+CreateifNotExistTableForLocation);

           ///New Update
            if(!isColumnExists(db,SalesmanChemistListTable,KEY_CHEMIST_LOCATION_FLAG)){
                db.execSQL("ALTER TABLE SalesmanChemistList ADD COLUMN locationFlag TEXT;");
            }
       ///////////Drop




        }
    }
    public boolean isColumnExists (SQLiteDatabase db,String table, String column) {

        Cursor cursor = db.rawQuery("PRAGMA table_info("+ table +")", null);

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex("name"));

                if (column.equalsIgnoreCase(name)) {

                    return true;

                }

            }

        }



        return false;

    }
    /*  @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        if ( newVersion >= 2) {
            db.execSQL("Create Unique Index idx_itemcode on " + TABLE_SALESMAN_PRODUCTS +"("+F_KEY_SS_PRODUCT_ID+")");
        }*/
        /*
        db.execSQL("DROP TABLE IF EXISTS " + ChemistListForCreateOrderSalesmanTable);
        db.execSQL("DROP TABLE IF EXISTS " + SalesmanChemistListTable);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlacedOrders);
        db.execSQL("DROP TABLE IF EXISTS " + TableInvoiceListDelivery);
        db.execSQL("DROP TABLE IF EXISTS " + TableSaveDeliveryOffline);
        db.execSQL("DROP TABLE IF EXISTS " + TableInvoiceListPayment);
        //db.execSQL("DROP TABLE IF EXISTS " + TableInvoicesPayment);
        db.execSQL("DROP TABLE IF EXISTS " + TableCollectPaymentSalesman);
        db.execSQL("DROP TABLE IF EXISTS " + TableCollectPaymentSelectedInvoice);
        db.execSQL("DROP TABLE IF EXISTS " + TableDeliveryScheduleSalesman);
        db.execSQL("DROP TABLE IF EXISTS " + TableDeliveryScheduleInvoices);
        db.execSQL("DROP TABLE IF EXISTS " + TableCollectDeliverySalesman);
        db.execSQL("DROP TABLE IF EXISTS " + TableCollectDeliverySelectedInvoice);
        */
  //  }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void inset_into_stockist_wise_products(String item_code, String stockist_id, String product_id, String item_name, String pack_size, String mrp,
                                                  String rate, String stock, String type, String mfg_code, String mfg_name, String dosage_form,
                                                  String scheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.setLockingEnabled(false);

        ContentValues values = new ContentValues();
        values.put(F_KEY_SP_ITEMCODE, item_code);
        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        values.put(F_KEY_SP_PRODUCT_ID, product_id);
        values.put(F_KEY_SP_ITEMNAME, item_name);
        values.put(F_KEY_SP_PACKSIZE, pack_size);
        values.put(F_KEY_SP_MRP, mrp);
        values.put(F_KEY_SP_RATE, rate);
        values.put(F_KEY_SP_STOCK, stock);
        values.put(F_KEY_SP_TYPE, type);
        values.put(F_KEY_SP_MFGCODE, mfg_code);
        values.put(F_KEY_SP_MFGNAME, mfg_name);
        values.put(F_KEY_SP_DOSEFORM, dosage_form);
        values.put(F_KEY_SP_SCHEME, scheme);
        // Inserting Row
        //long id = db.insert(TABLE_STOCKIST_WISE_PRODUCTS, null, values);
        db.insert(TABLE_STOCKIST_WISE_PRODUCTS, null, values);
        //Log.d("InsertValues", ""+values);
        // db.close(); //
    }


    /* Insert Into Chemist Product List */
    public void insertIntoChemistProductListByStockist(String item_code, String stockist_id, String product_id, String item_name, String pack_size, String mrp,
                                                       String rate, String stock, String type, String mfg_code, String mfg_name, String dosage_form,
                                                       String scheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.setLockingEnabled(false);
        ContentValues values = new ContentValues();
        values.put(KEY_ITEMCODE_CHEMIST_PRO, item_code);
        values.put(KEY_STOCKIST_ID_CHEMIST_PRO, stockist_id);
        values.put(KEY_PRODUCT_ID_CHEMIST_PRO, product_id);
        values.put(KEY_ITEMNAME_CHEMIST_PRO, item_name);
        values.put(KEY_PACKSIZE_CHEMIST_PRO, pack_size);
        values.put(KEY_MRP_CHEMIST_PRO, mrp);
        values.put(KEY_RATE_CHEMIST_PRO, rate);
        values.put(KEY_STOCK_CHEMIST_PRO, stock);
        values.put(KEY_TYPE_CHEMIST_PRO, type);
        values.put(KEY_MFGCODE_CHEMIST_PRO, mfg_code);
        values.put(KEY_MFGNAME_CHEMIST_PRO, mfg_name);
        values.put(KEY_DOSEFORM_CHEMIST_PRO, dosage_form);
        values.put(KEY_SCHEME_CHEMIST_PRO, scheme);
        // Inserting Row
        //long id = db.insert(ChemistStockistWiseProduct, null, values);
        db.insert(ChemistStockistWiseProduct, null, values);
        //Log.d("InsertValues", ""+values);
    }


    public void inset_into_salesman_products(String product_id, String item_code, String stockist_id, String item_name, String pack_size,
                                             String mrp, String rate, String stock, String type, String mfg_code, String mfg_name,
                                             String dosage_form, String scheme, String HalfScheme, String PercentScheme, String LegendName, String LegendColor, String BoxSize,String minQ,String maxQ) {
        //  SQLiteDatabase db = this.getWritableDatabase();
        //db.setLockingEnabled(false);

        ContentValues values = new ContentValues();
        values.put(F_KEY_SS_PRODUCT_ID, product_id);
        values.put(F_KEY_SS_ITEMCODE, item_code);
        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        values.put(F_KEY_SS_ITEMNAME, item_name);
        values.put(F_KEY_SS_PACKSIZE, pack_size);
        values.put(F_KEY_SS_MRP, mrp);
        values.put(F_KEY_SS_RATE, rate);
        values.put(F_KEY_SS_STOCK, stock);
        values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SS_MFGCODE, mfg_code);
        values.put(F_KEY_SS_MFGNAME, mfg_name);
        values.put(F_KEY_SS_DOSEFORM, dosage_form);
        values.put(F_KEY_SS_SCHEME, scheme);
        values.put(key_product_halfscheme, HalfScheme);
        values.put(key_product_percentscheme, PercentScheme);
        values.put(key_product_legendname, LegendName);
        values.put(key_product_legendcolor, LegendColor);
        values.put(key_product_BoxSize, BoxSize);
        values.put(key_product_minQ,minQ);
        values.put(key_product_maxQ,maxQ);

        mydb.insert(TABLE_SALESMAN_PRODUCTS, null, values);

        // Log.d("InsertValues", ""+values);
        //db.close();
    }

    public void insetupdate_into_salesman_products(String product_id, String item_code, String item_name, String pack_size,
                                             String mrp, String rate, String stock, String mfg_code, String mfg_name,
                                             String dosage_form, String scheme, String HalfScheme, String PercentScheme, String LegendName, String LegendColor,String BoxSize,String type,String stockist_id) {
          SQLiteDatabase db = this.getWritableDatabase();
        //db.setLockingEnabled(false);

        ContentValues values = new ContentValues();
        values.put(F_KEY_SS_PRODUCT_ID, product_id);
        values.put(F_KEY_SS_ITEMCODE, item_code);
//        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        values.put(F_KEY_SS_ITEMNAME, item_name);
        values.put(F_KEY_SS_PACKSIZE, pack_size);
        values.put(F_KEY_SS_MRP, mrp);
        values.put(F_KEY_SS_RATE, rate);
        values.put(F_KEY_SS_STOCK, stock);
        //values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SS_MFGCODE, mfg_code);
        values.put(F_KEY_SS_MFGNAME, mfg_name);
        values.put(F_KEY_SS_DOSEFORM, dosage_form);
        values.put(F_KEY_SS_SCHEME, scheme);
        values.put(key_product_halfscheme, HalfScheme);
        values.put(key_product_percentscheme, PercentScheme);
        values.put(key_product_legendname, LegendName);
        values.put(key_product_legendcolor, LegendColor);
        values.put(key_product_BoxSize,BoxSize);
        values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
       db.insert(TABLE_SALESMAN_PRODUCTS, null, values);

        // Log.d("InsertValues", ""+values);
        db.close();
    }

    public void insetupdate_delete_into_salesman_products(String product_id, String item_code, String item_name, String pack_size,
                                                          String mrp, String rate, String stock, String mfg_code, String mfg_name,
                                                          String dosage_form, String scheme, String HalfScheme, String PercentScheme, String LegendName, String LegendColor,String BoxSize,String type,String stockist_id) {
        //db.setLockingEnabled(false);

        // mydb.delete(TABLE_SALESMAN_PRODUCTS, F_KEY_SS_PRODUCT_ID + "= '" + product_id + "'", null);

        ContentValues values = new ContentValues();
        values.put(F_KEY_SS_PRODUCT_ID, product_id);
        values.put(F_KEY_SS_ITEMCODE, item_code);
//        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        values.put(F_KEY_SS_ITEMNAME, item_name);
        values.put(F_KEY_SS_PACKSIZE, pack_size);
        values.put(F_KEY_SS_MRP, mrp);
        values.put(F_KEY_SS_RATE, rate);
        values.put(F_KEY_SS_STOCK, stock);
        //values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SS_MFGCODE, mfg_code);
        values.put(F_KEY_SS_MFGNAME, mfg_name);
        values.put(F_KEY_SS_DOSEFORM, dosage_form);
        values.put(F_KEY_SS_SCHEME, scheme);
        values.put(key_product_halfscheme, HalfScheme);
        values.put(key_product_percentscheme, PercentScheme);
        values.put(key_product_legendname, LegendName);
        values.put(key_product_legendcolor, LegendColor);
        values.put(key_product_BoxSize,BoxSize);
        values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        mydb.update(TABLE_SALESMAN_PRODUCTS, values, F_KEY_SS_PRODUCT_ID.trim() + " = ?", new String[]{product_id.trim()});
        // Log.d("InsertValues", ""+values);
    }


///CreateForLocation

    public void insetDataForLocation(String user_id, String cust_id, String tansaction_id,
                                      String task,String Latitiude,String Longitude,String uniq_id,String address,String doc_id) {
        SQLiteDatabase db=null;
        Log.e("doc_id",""+doc_id);
        try {
             db = this.getWritableDatabase();

            //db.setLockingEnabled(false);

            ContentValues values = new ContentValues();
            values.put(Key_USERID, user_id);
            values.put(Key_CUSTID, cust_id);
            values.put(Key_Address, address);
            values.put(Key_TASK, task);
            values.put(Key_TRANSACTION_ID, tansaction_id);
            values.put(Key_LATITUDE, Latitiude);
            values.put(Key_LONGITUDE, Longitude);
            values.put(F_KEY_CC_DOC_NO ,doc_id);
            values.put(Key_UNIQ_ID, uniq_id);

            db.insert(TableForLocation, null, values);
            Log.e("TableForLocation","insert");
            // Log.d("InsertValues", ""+values);
            db.close();
        }catch (SQLiteException e){
            Log.e("TabForLocati","EXp"+e.toString());
            db.close();
        }


    }

    public void insert_into_stockist_legend(String Stockist_id, String legend_name, String color_code,
                                            String start_range,
                                            String end_range, String stockist_legend_id,
                                            String legend_mode) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(F_KEY_LM_STOCKIST_ID, Stockist_id);
        values.put(F_KEY_LM_LEGENDNAME, legend_name);
        values.put(F_KEY_LM_COLOR_CODE, color_code);
        values.put(F_KEY_LM_START_RANGE, start_range);
        values.put(F_KEY_LM_END_RANGE, end_range);
        values.put(F_KEY_LM_STOCKLEGENDID, stockist_legend_id);
        values.put(F_KEY_LM_LEGENDMODE, legend_mode);

        long id = db.insert(TABLE_LEGEND_MASTER, null, values);
        // db.close(); //
    }


    public void insert_into_chemist_cart(String Doc_id, String stockist_id, String Order_amount,
                                         String Doc_date,
                                         Integer itemcount, String remark,
                                         Integer order_placed, Integer order_sync) {
        SQLiteDatabase db = this.getWritableDatabase();


        String countQuery = "SELECT * FROM " + TABLE_CHEMIST_CART + " where " + F_KEY_CC_DOC_NO + "='" + Doc_id + "'";

        int previous_itemcount = 0;
        float previous_price = 0;
        // Cursor c = db.rawQuery(countQuery, null);
        ContentValues values = new ContentValues();

       /* if (c.getCount() > 0) {
            c.moveToFirst();
            try {
                previous_itemcount = Integer.parseInt(c.getString(c.getColumnIndex("Items")));
                previous_price = Float.parseFloat(c.getString(c.getColumnIndex(F_KEY_CORD_PRICE)));
                Log.v("OLDItem", String.valueOf(previous_itemcount));
                values.put(F_KEY_CC_ITEM_COUNT, itemcount + previous_itemcount);
                values.put(F_KEY_CC_ORDER_AMOUNT, String.valueOf(Float.parseFloat(Order_amount) + previous_price));
                db.update(TABLE_CHEMIST_CART, values, "DOC_NO='" + Doc_id + "'", null);
                c.close();
                return;

            } catch (NumberFormatException nfe) {

                c.close();
            }

        }

        c.close();*/
        values.put(F_KEY_CC_DOC_NO, Doc_id);
        values.put(F_KEY_CC_STOCKISTID, stockist_id);
        values.put(F_KEY_CC_ORDER_AMOUNT, Order_amount);
        values.put(F_KEY_CC_DOC_DATE, Doc_date);
        values.put(F_KEY_CC_CREATED_ON, Doc_date);
        values.put(F_KEY_CC_STATUS, 0);
        values.put(F_KEY_CC_ITEM_COUNT, itemcount);
        values.put(F_KEY_CC_ORDER_PLACED, order_placed);
        values.put(F_KEY_CC_ORDER_SYNC, order_sync);
        values.put(F_KEY_CC_REMARK, remark);

        long id = db.insert(TABLE_CHEMIST_CART, null, values);
        // db.close(); //
    }


    public void insert_into_chemist_cart_details(String Doc_id, String item_number, String product_id,
                                                 Integer Quantity, String uom, String rate,
                                                 String price, String mrp,
                                                 String date) {
        SQLiteDatabase db = this.getWritableDatabase();


      /*  String countQuery = "SELECT  * FROM " + TABLE_CHEMIST_CART_DETAIL + " where " + F_KEY_CORD_PRODUCT_ID + "=?";
        int previous_itemcount = 0;
        float previous_price = 0;
        Cursor c = db.rawQuery(countQuery, new String[]{product_id});*/
        ContentValues values = new ContentValues();


       /* if (c.getCount() > 0) {
            c.moveToFirst();
            try {


                previous_itemcount = Integer.parseInt(c.getString(c.getColumnIndex(F_KEY_CORD_QTY)));
                previous_price = Float.parseFloat(c.getString(c.getColumnIndex(F_KEY_CORD_PRICE)));
                Log.v("OLDItem", String.valueOf(previous_itemcount));
                values.put(F_KEY_CORD_QTY, Quantity + previous_itemcount);
                values.put(F_KEY_CORD_PRICE, String.valueOf(Float.parseFloat(price) + previous_price));
                db.update(TABLE_CHEMIST_CART_DETAIL, values, "Product_ID='" + product_id + "'", null);
                c.close();
                return;


            } catch (NumberFormatException nfe) {

                c.close();


            }
        }

        c.close();
*/

        values.put(F_KEY_CORD_DOC_NO, Doc_id);
        values.put(F_KEY_CORD_DOC_ITEM_NO, item_number);
        values.put(F_KEY_CORD_PRODUCT_ID, product_id);
        values.put(F_KEY_CORD_QTY, Quantity);
        values.put(F_KEY_CORD_UOM, uom);
        values.put(F_KEY_CORD_RATE, rate);
        values.put(F_KEY_CORD_PRICE, price);
        values.put(F_KEY_CORD_MRP, mrp);
        values.put(F_KEY_CORD_CREATED_ON, date);

        long id = db.insert(TABLE_CHEMIST_CART_DETAIL, null, values);
        // db.close(); //
    }


    public void insert_into_chemist_order_sync(String doc_id, String json, Integer Synced) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(F_KEY_COS_DOC_ID, doc_id);
        values.put(F_KEY_COS_JSON, json);
        values.put(F_KEY_COS_SYNCED, Synced);

        long id = db.insert(TABLE_CHEMIST_ORDER_SYNC, null, values);
        //db.close(); //
    }


    /* Insert Into Chemist List Salesman For Create Order */
    public void insertChemistListSalesmanForCreateOrder(String userId, String chemistId, String custCode, String custName, String email,
                                                        String mobile, String location, String latitude, String longitude, String outStandingBill) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_ChemistListForCreateOrderSalesman, userId);
        values.put(Key_ChemistId_ChemistListForCreateOrderSalesman, chemistId);
        values.put(Key_CustCode_ChemistListForCreateOrderSalesman, custCode);
        values.put(Key_CustName_ChemistListForCreateOrderSalesman, custName);
        values.put(Key_Email_ChemistListForCreateOrderSalesman, email);
        values.put(Key_Mobile_ChemistListForCreateOrderSalesman, mobile);
        values.put(Key_Location_ChemistListForCreateOrderSalesman, location);
        values.put(Key_Latitude_ChemistListForCreateOrderSalesman, latitude);
        values.put(Key_Longitude_ChemistListForCreateOrderSalesman, longitude);
        values.put(Key_OutstandingBill_ChemistListForCreateOrderSalesman, outStandingBill);
      /*  values.put(Key_EXPIRY_DATE,DLExpiry);
        values.put(Key_EXPIRY_ID,DLExpInd);*/
        database.insert(ChemistListForCreateOrderSalesmanTable, null, values);
        //Log.d("CreateOrderChemistList", ""+values);
    }


    /* Insert Into SalesmanChemist */
    public void insertChemistList(String userId, String chemistId, String stockistCallPlanId, String dayNumber, String chemistName,
                                  String latitude, String longitude, String address, String profileImage, String inTime, String outTime,
                                  String status, String billAmount, String sequence, String taskStatus, String orderReceived,
                                  String passedDate, String isLocked,String DLExpiry,String Block_reason) {
       // SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_SalesmanChemistList, userId);
        values.put(Key_ChemistId_SalesmanChemistList, chemistId);
        values.put(Key_StockistCallPlanID_SalesmanChemistList, stockistCallPlanId);
        values.put(Key_DayNumber_SalesmanChemistList, dayNumber);
        values.put(Key_ChemistName_SalesmanChemistList, chemistName);
        values.put(Key_Latitude_SalesmanChemistList, latitude);
        values.put(Key_Longitude_SalesmanChemistList, longitude);
        values.put(Key_Address_SalesmanChemistList, address);
        values.put(Key_ProfileImage_SalesmanChemistList, profileImage);
        values.put(Key_InTime_SalesmanChemistList, inTime);
        values.put(Key_OutTime_SalesmanChemistList, outTime);
        values.put(Key_Status_SalesmanChemistList, status);
        values.put(Key_BillAmount_SalesmanChemistList, billAmount);
        values.put(Key_Sequence_SalesmanChemistList, sequence);
        values.put(Key_TaskStatus_SalesmanChemistList, taskStatus);
        values.put(Key_OrderRecevied_SalesmanChemistList, orderReceived);
        values.put(Key_PassedDate_SalesmanChemistList, passedDate);
        values.put(Key_isLocked_SalesmanChemistList, isLocked);
        values.put(Key_DLExpiry_SalesmanChemistList, DLExpiry);
        values.put(Key_Block_Reason_SalesmanChemistList,Block_reason);
        if(latitude.equalsIgnoreCase("0.0")&&longitude.equalsIgnoreCase("0.0")){
            values.put(KEY_CHEMIST_LOCATION_FLAG,"N");
        }else{
            values.put(KEY_CHEMIST_LOCATION_FLAG,"Y");
        }

        mydb.insert(SalesmanChemistListTable, null, values);
        Log.d("chemistValues", ""+values);
    }


    /* Insert Order Offline */
    public void insertOrder(String userId, String createdBy, String clientId, String chemistId, String chemistName, String docNo, String docDate,
                            String remarks, String items, String amount, String status, String createdOn, String deliveryOption, String itemsJson,
                            String itemsCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_PlacedOrder, userId);
        values.put(Key_CreatedBy_PlacedOrder, createdBy);
        values.put(Key_ClientId_PlacedOrder, clientId);
        values.put(Key_ChemistId_PlacedOrder, chemistId);
        values.put(Key_ChemistName_PlacedOrder, chemistName);
        values.put(Key_DocNo_PlacedOrder, docNo);
        values.put(Key_DocDate_PlacedOrder, docDate);
        values.put(Key_Remarks_PlacedOrder, remarks);
        values.put(Key_Items_PlacedOrder, items);
        values.put(Key_Amount_PlacedOrder, amount);
        values.put(Key_Status_PlacedOrder, status);
        values.put(Key_CreatedOn_PlacedOrder, createdOn);
        values.put(Key_DeliveryOption_PlacedOrder, deliveryOption);
        values.put(Key_ItemsJson_PlacedOrder, itemsJson);
        values.put(Key_ItemCodes_PlacedOrder, itemsCode);
        db.insert(TablePlacedOrders, null, values);
        //Log.d("placedOrders", ""+values);
    }


    /* Insert Invoice List Delivery */
    public void insertInvoiceListDelivery(String userId, String stockistId, String chemistId, String orderNo, String chemistName, String orderStatus,
                                          String invoiceDate, String invoiceNo, String invoiceAmount, String billStatus, String items, String orderDate,
                                          String orderAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_InvoiceListDelivery, userId);
        values.put(Key_StockistId_InvoiceListDelivery, stockistId);
        values.put(Key_ChemistId_InvoiceListDelivery, chemistId);
        values.put(Key_OrderNo_InvoiceListDelivery, orderNo);
        values.put(Key_ChemistName_InvoiceListDelivery, chemistName);
        values.put(Key_OrderStatus_InvoiceListDelivery, orderStatus);
        values.put(Key_InvoiceDate_InvoiceListDelivery, invoiceDate);
        values.put(Key_InvoiceNo_InvoiceListDelivery, invoiceNo);
        values.put(Key_InvoiceAmount_InvoiceListDelivery, invoiceAmount);
        values.put(Key_BillStatus_InvoiceListDelivery, billStatus);
        values.put(Key_Items_InvoiceListDelivery, items);
        values.put(Key_OrderDate_InvoiceListDelivery, orderDate);
        values.put(Key_OrderAmount_InvoiceListDelivery, orderAmount);
        db.insert(TableInvoiceListDelivery, null, values);
    }


    /* Insert Into Delivery Table */
    public void insertIntoSaveDelivery(String userId, String startTime, String endTime, String customerName, String stockistClientId,
                                       String chemistId, String callPlanId, String transactionNo, String invoiceNo, String orderDate,
                                       String itemCount, String invoiceAmount, String status, String description, String orderImg, String signImg,
                                       String currentTime, String currentLat, String currentLong, String noOfPackets, String noOfCases) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId, userId);
        values.put(Key_StartTime, startTime);
        values.put(Key_EndTime, endTime);
        values.put(Key_CustomerName, customerName);
        values.put(Key_StockistClientId, stockistClientId);
        values.put(Key_ChemistId, chemistId);
        values.put(Key_CallPlanId, callPlanId);
        values.put(Key_TransactionNo, transactionNo);
        values.put(Key_InvoiceNo, invoiceNo);
        values.put(Key_OrderDate, orderDate);
        values.put(Key_ItemCount, itemCount);
        values.put(Key_InvoiceAmount, invoiceAmount);
        values.put(Key_Status, status);
        values.put(Key_Description, description);
        values.put(Key_OrderImg, orderImg);
        values.put(Key_SignImg, signImg);
        values.put(Key_SavedTime, currentTime);
        values.put(Key_CurrentLocLat, currentLat);
        values.put(Key_CurrentLocLong, currentLong);
        values.put(Key_NoOfPackets, noOfPackets);
        values.put(Key_NoOfCases, noOfCases);

        db.insert(TableSaveDeliveryOffline, null, values);
        //Log.d("DeliveryItems", ""+values);
    }


    /* Insert Invoice List Payment */
    public void insertInvoiceListPayment(String userId, String stockistId, String chemistId, String invoiceNo, String invoiceId,
                                         String invoiceDate, String totalItems, String billAmount, String paymentReceived,
                                         String balanceAmount, String paymentStatus, String grandTotal, String totalDiscount, String ledgerBalance, String invoice_flag) {
        // SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_InvoiceListPayment, userId);
        values.put(Key_StockistId_InvoiceListPayment, stockistId);
        values.put(Key_ChemistId_InvoiceListPayment, chemistId);
        values.put(Key_InvoiceNo_InvoiceListPayment, invoiceNo);
        values.put(Key_InvoiceId_InvoiceListPayment, invoiceId);
        values.put(Key_InvoiceDate_InvoiceListPayment, invoiceDate);
        values.put(Key_TotalItems_InvoiceListPayment, totalItems);
        values.put(Key_BillAmount_InvoiceListPayment, billAmount);
        values.put(Key_PaymentReceived_InvoiceListPayment, paymentReceived);
        values.put(Key_BalanceAmount_InvoiceListPayment, balanceAmount);
        values.put(Key_PaymentStatus_InvoiceListPayment, paymentStatus);
        values.put(Key_GrandTotal_InvoiceListPayment, grandTotal);
        values.put(Key_TotalDiscount_InvoiceListPayment, totalDiscount);
        values.put(Key_LedgerBalance_InvoiceListPayment, ledgerBalance);
        values.put(Key_PaymentFlag_InvoiceListPayment, invoice_flag);
        mydb.insert(TableInvoiceListPayment, null, values);
        Log.d("TableInvoiceListPayt1", values.toString());
        Log.d("invoiceNo1", invoiceNo);
        //Log.d("invoiceListSQL", ""+values);
    }



    /* Insert Into Invoice  */
    /*public void insertInvoice(String userId, String stockistId, String chemistId, String invoiceNo, String invoiceId, String invoiceDate,
                              String currentDate, String totalItems, String cashReceived, String totalAmount, String balanceAmount,
                              String paymentModeId, String paymentMode, String chequeDate, String chequeNo, String bankId, String bankName,
                              String paymentId, String narration, String status, String micrNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_InvoicePayment, userId);
        values.put(Key_StockistId_InvoicePayment, stockistId);
        values.put(Key_ChemistId_InvoicePayment, chemistId);
        values.put(Key_InvoiceNo_InvoicePayment, invoiceNo);
        values.put(Key_InvoiceId_InvoicePayment, invoiceId);
        values.put(Key_InvoiceDate_InvoicePayment, invoiceDate);
        values.put(Key_SavedDate_InvoicePayment, currentDate);
        values.put(Key_TotalItems_InvoicePayment, totalItems);
        values.put(Key_CashReceived_InvoicePayment, cashReceived);
        values.put(Key_TotalAmount_InvoicePayment, totalAmount);
        values.put(Key_BalanceAmount_InvoicePayment, balanceAmount);
        values.put(Key_PaymentModeId_InvoicePayment, paymentModeId);
        values.put(Key_PaymentMode_InvoicePayment, paymentMode);
        values.put(Key_ChequeDate_InvoicePayment, chequeDate);
        values.put(Key_ChequeNo_InvoicePayment, chequeNo);
        values.put(Key_BankId_InvoicePayment, bankId);
        values.put(Key_BankName_InvoicePayment, bankName);
        values.put(Key_PaymentId_InvoicePayment, paymentId);
        values.put(Key_Narration_InvoicePayment, narration);
        values.put(Key_Status_InvoicePayment, status);
        values.put(Key_MicrNo_InvoicePayment, micrNo);
        db.insert(TableInvoicesPayment, null, values);
        Log.d("InvoiceItems", ""+values);
    }*/


    public void insertPaymentCollection(String stockistId, String userId, String chemistId, String paymentId, String createdBy,
                                        String amount, String paymentDate, String remarks, String status, String createdDate,
                                        String paymentMode, String bankName, String chequeNo, String chequeAmount,
                                        String chequeDate, String micrNo, String narration, String comments, String Flag, String invoiceList , String chemistName) {
        // SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_StockistId_PaymentSalesman, stockistId);
        values.put(Key_UserId_PaymentSalesman, userId);
        values.put(Key_ChemistId_PaymentSalesman, chemistId);
        values.put(Key_PaymentId_PaymentSalesman, paymentId);
        values.put(Key_CreatedBy_PaymentSalesman, createdBy);
        values.put(Key_Amount_PaymentSalesman, amount);
        values.put(Key_PaymentDate_PaymentSalesman, paymentDate);
        values.put(Key_Remarks_PaymentSalesman, remarks);
        values.put(Key_Status_PaymentSalesman, status);
        values.put(Key_CreatedDate_PaymentSalesman, createdDate);
        values.put(Key_PaymentMode_PaymentSalesman, paymentMode);
        values.put(Key_BankName_PaymentSalesman, bankName);
        values.put(Key_ChequeNo_PaymentSalesman, chequeNo);
        values.put(Key_ChequeAmount_PaymentSalesman, chequeAmount);
        values.put(Key_ChequeDate_PaymentSalesman, chequeDate);
        values.put(Key_MicrNo_PaymentSalesman, micrNo);
        values.put(Key_Narration_PaymentSalesman, narration);
        values.put(Key_Comments_PaymentSalesman, comments);
        values.put(Key_Flag_PaymentSalesman, Flag);
        values.put(Key_InvoiceList_PaymentSalesman, invoiceList);
        values.put(Key_ChemistName_PaymentSalesman, chemistName);
        mydb.insert(TableCollectPaymentSalesman, null, values);
        //database.close();
    }
    /* Insert Selected Invoices */
    public void insertSelectedInvoices(String stockistId, String userId, String chemistId, String paymentId, String invoiceNo,
                                       String paidAmount, String paymentStatus, String createdDate, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_StockistId_PaymentSelectedInvoice, stockistId);
        values.put(Key_UserId_PaymentSelectedInvoice, userId);
        values.put(Key_ChemistId_PaymentSelectedInvoice, chemistId);
        values.put(Key_PaymentId_PaymentSelectedInvoice, paymentId);
        values.put(Key_InvoiceNo_PaymentSelectedInvoice, invoiceNo);
        values.put(Key_PaidAmount_PaymentSelectedInvoice, paidAmount);
        values.put(Key_PaymentStatus_PaymentSelectedInvoice, paymentStatus);
        values.put(Key_CreatedDate_PaymentSelectedInvoice, createdDate);
        values.put(Key_Status_PaymentSelectedInvoice, status);
        database.insert(TableCollectPaymentSelectedInvoice, null, values);
        database.close();
    }
    /*  Delivery Schedule Salesman */
    public void insertTableDeliveryScheduleSalesman(String clientId, String userId, String clientCode, String clientName,
                                                    String clientAddress)
    {
        // SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // values.put(Key_Id_DeliverySchedule, Id);
        values.put(Key_ClientId_DeliverySchedule, clientId);
        values.put(Key_UserID_DeliverySchedule, userId);
        values.put(Key_Client_Code_DeliverySchedule, clientCode);
        values.put(Key_Client_Name_DeliverySchedule, clientName);
        values.put(Key_Client_Address_DeliverySchedule, clientAddress);

        mydb.insert(TableDeliveryScheduleSalesman, null, values);
        Log.d("customerListSQLDelivery", ""+values);
    }
    /*  Delivery Schedule Invoices */
    /* Insert  Invoice List Delivery */
    public void insertDeliveryScheduleInvoices(String userId,String chemistId, String salesmanId, String deliveryDate,String deliveryStatus, String orderId, String invoiceDate,String boxCount,
                                               String invoiceNo, String totalAmount, String totalItems, String packageCount,
                                               String deliveryId,String deliveryFlag,String deliveryDescription,String client_name) {
        //  SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_DeliveryScheduleInvoices, userId);
        values.put(Key_ClientID_DeliveryScheduleInvoices, chemistId);
        values.put(Key_ErpsalesmanID_DeliveryScheduleInvoices, salesmanId);
        values.put(Key_DeliveryDate_DeliveryScheduleInvoices, deliveryDate);
        values.put(Key_DeliveryStatus_DeliveryScheduleInvoices, deliveryStatus);
        values.put(Key_OrderID_DeliveryScheduleInvoices, orderId);
        values.put(Key_InvoiceDate_DeliveryScheduleInvoices, invoiceDate);
        values.put(Key_BoxCount_DeliveryScheduleInvoices, boxCount);
        values.put(Key_InvoiceNo_DeliveryScheduleInvoices, invoiceNo);
        values.put(Key_TotalAmount_DeliveryScheduleInvoices, totalAmount);
        values.put(Key_TotalItems_DeliveryScheduleInvoices, totalItems);
        values.put(Key_Package_count_DeliveryScheduleInvoices, packageCount);
        values.put(Key_DeliveryId_DeliveryScheduleInvoices, deliveryId);
        values.put(Key_DeliveryFlag_DeliveryScheduleInvoices, deliveryFlag);
        values.put(Key_DeliveryDescription_DeliveryScheduleInvoices, deliveryDescription);
        values.put(Key_ClientNAME_DeliveryScheduleInvoices, client_name);
        mydb.insert(TableDeliveryScheduleInvoices, null, values);
        // db.close();
    //    Log.d("invoiceListSQLDelivery", ""+values);
    }

    /*  insertDeliveryCollection   */
    public void insertDeliveryCollection(String stockistId, String userId, String chemistId, String Delivery_doc_no, String createdBy,
                                         String Package_count, String deliveryDate, String DeliveryStatus, String Order_Image, String Sign_Image,
                                         String Latitude, String Longitude, String Description, String Flag) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_StockistId_DeliverySalesman, stockistId);
        values.put(Key_UserId_DeliverySalesman, userId);
        values.put(Key_ChemistId_DeliverySalesman, chemistId);
        values.put(Key_DeliveryId_DeliverySalesman, Delivery_doc_no);
        values.put(Key_CreatedBy_DeliverySalesman, createdBy);
        values.put(Key_Package_count_DeliverySalesman, Package_count);
        values.put(Key_Delivery_date_DeliverySalesman, deliveryDate);
        values.put(Key_DeliveryStatus_DeliverySalesman, DeliveryStatus);
        values.put(Key_Order_Image_DeliverySalesman, Order_Image);
        values.put(Key_Sign_Image_DeliverySalesman, Sign_Image);
        values.put(Key_Latitude_DeliverySalesman, Latitude);
        values.put(Key_Longitude_DeliverySalesman, Longitude);
        values.put(Key_Description_DeliverySalesman, Description);
        values.put(Key_Flag_DeliverySalesman, Flag);
        Log.d("insertDelivery1",values.toString());
        database.insert(TableCollectDeliverySalesman, null, values);
        database.close();
    }

    /*  insertDeliveryCollection  Invoice  */


    public void insertSelectedDeliveryInvoices(String stockistId, String userId, String chemistId, String Delivery_doc_no, String Delivery_Invoice_no,
                                               String Delivery_ID,String Delivery_Status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_StockistId_DeliverySelectedInvoice, stockistId);
        values.put(Key_UserId_DeliverySelectedInvoice, userId);
        values.put(Key_ChemistId_DeliverySelectedInvoice, chemistId);
        values.put(Key_DeliveryId_DeliverySelectedInvoice, Delivery_doc_no);
        values.put(Key_InvoiceNo_DeliverySelectedInvoice, Delivery_Invoice_no);
        values.put(Key_Delivery_KeyID_DeliverySelectedInvoice, Delivery_ID);
        values.put(Key_Status_DeliverySelectedInvoice, Delivery_Status);
        //  values.put(Key_Delivery_Description_DeliverySelectedInvoice, Delivery_Description);

        Log.d("insertDelivery",values.toString());
        database.insert(TableCollectDeliverySelectedInvoice, null, values);
        database.close();
    }

    public void deletePaymentRecord(String paymentId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableCollectPaymentSalesman, Key_PaymentId_PaymentSalesman + " = ?", new String[]{paymentId});
        database.close();
    }


    /* Delete Selected Invoices By Payment Id */
    public void deleteSelectedInvoiceByPaymentId(String paymentId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableCollectPaymentSelectedInvoice, Key_PaymentId_PaymentSelectedInvoice + " = ?", new String[]{paymentId});
        database.close();
    }

    public JSONObject getLocationDataby(String chemistId){
        SQLiteDatabase database = this.getReadableDatabase();
        JSONObject jsonParams = new JSONObject();

        Cursor cursor = database.query(TableForLocation,
                new String[]{Key_USERID, Key_CUSTID, Key_TASK,Key_Address,F_KEY_CC_DOC_NO,Key_TRANSACTION_ID,
                        Key_LATITUDE,Key_LONGITUDE,Key_UNIQ_ID},
                Key_CUSTID + "=?",
                new String[]{String.valueOf(chemistId)}, null, null, null, null);


        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {

                    try {
                        jsonParams.put("UserID", cursor.getString(cursor.getColumnIndex(Key_USERID)));
                        jsonParams.put("CustID", Integer.valueOf(cursor.getString(cursor.getColumnIndex(Key_CUSTID))));
                        jsonParams.put("task", cursor.getString(cursor.getColumnIndex(Key_TASK)));
                        jsonParams.put("CurrentLocation", cursor.getString(cursor.getColumnIndex(Key_Address)));
                        jsonParams.put("DOC_NO", cursor.getString(cursor.getColumnIndex(F_KEY_CC_DOC_NO)));
                        jsonParams.put("Tran_No", cursor.getString(cursor.getColumnIndex(Key_TRANSACTION_ID)));
                        jsonParams.put("Latitude", cursor.getString(cursor.getColumnIndex(Key_LATITUDE)));
                        jsonParams.put("Longitude", cursor.getString(cursor.getColumnIndex(Key_LONGITUDE)));
                        jsonParams.put("unqid", cursor.getString(cursor.getColumnIndex(Key_UNIQ_ID)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
        }
        return jsonParams;


    }

    // COMMENT THIS CODE 23 OCT
    /*public JSONObject getLocationDatabyDocId(String doc_id){
        SQLiteDatabase database = this.getReadableDatabase();
        JSONObject jsonParams = new JSONObject();

        Cursor cursor = database.query(TableForLocation,
                new String[]{Key_USERID, Key_CUSTID, Key_TASK,Key_Address,F_KEY_CC_DOC_NO,Key_TRANSACTION_ID,
                        Key_LATITUDE,Key_LONGITUDE,Key_UNIQ_ID},
                F_KEY_CC_DOC_NO + "=?",
                new String[]{String.valueOf(doc_id)}, null, null, null, null);




         if(cursor!=null) {
             if (cursor.moveToFirst()) {
                 do {

                     try {
                         jsonParams.put("UserID", cursor.getString(cursor.getColumnIndex(Key_USERID)));
                         jsonParams.put("CustID", Integer.valueOf(cursor.getString(cursor.getColumnIndex(Key_CUSTID))));
                         jsonParams.put("task", cursor.getString(cursor.getColumnIndex(Key_TASK)));
                         jsonParams.put("CurrentLocation", cursor.getString(cursor.getColumnIndex(Key_Address)));
                         jsonParams.put("DOC_NO", cursor.getString(cursor.getColumnIndex(F_KEY_CC_DOC_NO)));
                         jsonParams.put("Tran_No", cursor.getString(cursor.getColumnIndex(Key_TRANSACTION_ID)));
                         jsonParams.put("Latitude", cursor.getString(cursor.getColumnIndex(Key_LATITUDE)));
                         jsonParams.put("Longitude", cursor.getString(cursor.getColumnIndex(Key_LONGITUDE)));
                         jsonParams.put("unqid", cursor.getString(cursor.getColumnIndex(Key_UNIQ_ID)));

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 } while (cursor.moveToNext());
             }
         }
        return jsonParams;


    }*/

    public Cursor getLocationDatabyDocId(String doc_id){
        SQLiteDatabase database = this.getReadableDatabase();
        JSONObject jsonParams = new JSONObject();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TableForLocation + " WHERE " + F_KEY_CC_DOC_NO +
                " = '" + doc_id + "'", null);
        return cursor;



    }

    public Cursor getCollectedPaymentRecord(String Flag) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman + " WHERE " + Key_Flag_PaymentSalesman +
                " = '" + Flag + "'", null);
        //return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman, null);
    }


    /* Get Selected Invoices */
    public Cursor getSelectedInvoiceByPaymentId(String paymentId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableCollectPaymentSelectedInvoice + " WHERE " + Key_PaymentId_PaymentSelectedInvoice +
                " = '" + paymentId + "'", null);
    }


    public void updateInvoiceReceivedAmount1(String invoiceNo, String receivedAmount) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableInvoiceListPayment + " SET " + Key_PaymentReceived_InvoiceListPayment + " = " +
                Key_PaymentReceived_InvoiceListPayment + " + " + receivedAmount + " WHERE " +
                Key_InvoiceNo_InvoiceListPayment + " = '" + invoiceNo + "'";
        Log.d("updatedQuery", query);
        database.execSQL(query);
        database.close();
        updateInvoiceBalanceAmount(invoiceNo);
    }
    /*  Update flag status for daily collection reports    */
    public void updateFlagCollectPaymentSalesman(String paymentId, String flag) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableCollectPaymentSalesman + " SET " + Key_Flag_PaymentSalesman + " = " +
                "'" + flag + "'" + " WHERE " +
                Key_PaymentId_PaymentSalesman + " = '" + paymentId + "'";

        Log.d("updatedQuery", query);
        database.execSQL(query);
        database.close();

    }
    /* Update Product List In Salesman Section */
    public void updateInvoiceReceivedAmount(String userId, String stockistId, String chemistId, String invoiceNo, String invoiceId,
                                            String invoiceDate, String totalItems, String billAmount, String paymentReceived,
                                            String balanceAmount, String paymentStatus, String grandTotal, String totalDiscount, String ledgerBalance, String invoice_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_UserId_InvoiceListPayment, userId);
        values.put(Key_StockistId_InvoiceListPayment, stockistId);
        values.put(Key_ChemistId_InvoiceListPayment, chemistId);
        values.put(Key_InvoiceNo_InvoiceListPayment, invoiceNo);
        values.put(Key_InvoiceId_InvoiceListPayment, invoiceId);
        values.put(Key_InvoiceDate_InvoiceListPayment, invoiceDate);
        values.put(Key_TotalItems_InvoiceListPayment, totalItems);
        values.put(Key_BillAmount_InvoiceListPayment, billAmount);
        values.put(Key_PaymentReceived_InvoiceListPayment, paymentReceived);
        values.put(Key_BalanceAmount_InvoiceListPayment, balanceAmount);
        values.put(Key_PaymentStatus_InvoiceListPayment, paymentStatus);
        values.put(Key_GrandTotal_InvoiceListPayment, grandTotal);
        values.put(Key_TotalDiscount_InvoiceListPayment, totalDiscount);
        values.put(Key_LedgerBalance_InvoiceListPayment, ledgerBalance);
        values.put(Key_PaymentFlag_InvoiceListPayment, invoice_flag);
        db.update(TableInvoiceListPayment, values, Key_InvoiceNo_InvoiceListPayment + " = ?", new String[]{invoiceNo});
        // db.close();
        Log.d("TableInvoiceListPayt", values.toString());
        Log.d("invoiceNo", invoiceNo);
    }

    /* Update Invoice Balance After ReceivedAmount */
    private void updateInvoiceBalanceAmount(String invoiceNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableInvoiceListPayment + " SET " + Key_BalanceAmount_InvoiceListPayment + " = " +
                Key_BillAmount_InvoiceListPayment + " - " + Key_PaymentReceived_InvoiceListPayment + " WHERE " +
                Key_InvoiceNo_InvoiceListPayment + " = '" + invoiceNo + "'";
        database.execSQL(query);
        database.close();
    }




    /* Get All Invoice By User Id */
    public Cursor getInvoiceByUserId(String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_UserId_InvoiceListPayment +
                " = " + userId, null);
    }
    /* Get Daily Collection By User Id */
    public Cursor getDailyCollectionByUserId(String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman + " WHERE " + Key_CreatedBy_PaymentSalesman +
                " = " + userId, null);
    }
    /* Get Delivery Schedule By User Id */
    public Cursor getDeliveryScheduleByUserId(String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleSalesman + " WHERE " + Key_UserID_DeliverySchedule +
                " = " + userId, null);
    }

    /* Get All Invoice Delivery By Chemist Id */
//    public Cursor getDeliveryInvoiceByChemistId(String chemistId) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
// Key_ClientID_DeliveryScheduleInvoices +
//                " = " + chemistId, null);
//    }
       public Cursor getDeliveryInvoiceByChemistId(String chemistId,String filterflag) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
                Key_ClientID_DeliveryScheduleInvoices + " = '" + chemistId + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " != "+ filterflag, null);
    }

    public Cursor getDeliveryInvoiceBySalesmanIdNew1(String userid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " + Key_UserId_DeliveryScheduleInvoices +
                " = " + userid, null);

    }
   /* public Cursor getDeliveryInvoiceBySalesmanId(String userid,String filterflag) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
                Key_UserId_DeliveryScheduleInvoices + " = '" + userid + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " != "+ filterflag + " Order By "+ "date"+"(" +Key_DeliveryDate_DeliveryScheduleInvoices+ " )" +" DESC", null);

        *//*return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
                Key_UserId_DeliveryScheduleInvoices + " = '" + userid + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " != "+ filterflag +" ORDER BY "+ Key_DeliveryDate_DeliveryScheduleInvoices, null);
*//*
       // Key_DeliveryDate_DeliveryScheduleInvoices
    }*/
   public Cursor getDeliveryInvoiceBySalesmanId(String userid,String filterflag) {
       SQLiteDatabase database = this.getReadableDatabase();
       return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
               Key_UserId_DeliveryScheduleInvoices + " = '" + userid + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " != "+ filterflag + " Order By "+ "date"+"(" +Key_DeliveryDate_DeliveryScheduleInvoices+ " )" +" DESC", null);

        /*return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
                Key_UserId_DeliveryScheduleInvoices + " = '" + userid + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " != "+ filterflag +" ORDER BY "+ Key_DeliveryDate_DeliveryScheduleInvoices, null);
*/
       // Key_DeliveryDate_DeliveryScheduleInvoices
   }
    public Cursor getDeliveryInvoiceBySalesmanIdNew(String userid) {
        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " + Key_DeliveryFlag_DeliveryScheduleInvoices +
//                " = " + userid, null);
        return database.rawQuery("SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " +
                Key_UserId_DeliveryScheduleInvoices + " = '" + userid + "'" + " AND " + Key_DeliveryFlag_DeliveryScheduleInvoices + " = '1'", null);
        //SELECT * FROM COMPANY WHERE AGE >= 25 OR SALARY >= 65000;
    }

    /* Get All Invoice List By ChemistId (Salesman) */
//    public Cursor getAllInvoice(String chemistId) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment +
//                " = " + chemistId +" ORDER BY ", null);
//    }


//jyott
//    public Cursor getAllInvoice(String chemistId) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment +
//                " = " + chemistId +"ORDER BY"+ Key_InvoiceDate_InvoiceListPayment + "DESC", null);
////ORDER BY updated_at DESC
//
//    }
    //jyott

    public Cursor getAllInvoice(String chemistId,String invoice_flag) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment +
                " WHERE " + Key_ChemistId_InvoiceListPayment + " = " + chemistId
                + " AND " + Key_PaymentFlag_InvoiceListPayment + " = " + invoice_flag
                + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
    }

//    public Cursor getCreditNote(String chemistId,String invoice_flag,String invoice_flag1) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment +
//                " WHERE " + Key_ChemistId_InvoiceListPayment + " = " + chemistId
//                + " AND " + Key_PaymentFlag_InvoiceListPayment + " = " + invoice_flag
//                + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
//    }

    /* Get Pending Payment Invoice List By Chemist Id (Salesman) */
    public Cursor getCreditNote(String chemistId,String invoice_flag,String invoice_flag1) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment +
                " WHERE " + Key_ChemistId_InvoiceListPayment + " = " + chemistId
                + " AND " +" ( "+ Key_PaymentFlag_InvoiceListPayment + " = " + invoice_flag
                + " OR " + Key_PaymentFlag_InvoiceListPayment + " = " + invoice_flag1 + " ) "
                + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
    }

    public Cursor getCreditNoteFlag(String chemistId,String invoice_flag) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment +
                " WHERE " + Key_ChemistId_InvoiceListPayment + " = " + chemistId
                + " AND " + Key_PaymentFlag_InvoiceListPayment + " != " + invoice_flag
                + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
    }


    //apurva where condition 1 and (condition2 or condition 3)
//    public Cursor getPendingInvoiceList(String chemistId) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment + " = '" +
//                chemistId + "'" + " AND " + Key_BalanceAmount_InvoiceListPayment + " > '0'" + " AND " +
//                Key_BalanceAmount_InvoiceListPayment + " < '" + Key_BillAmount_InvoiceListPayment + "'";
//        return database.rawQuery(query, null);
//    }


    /* Get Invoice By InvoiceId */
    public Cursor getInvoiceByInvoiceId(String invoiceId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_InvoiceId_InvoiceListPayment +
                " = " + invoiceId, null);


    }
    /* Get Invoice By InvoiceId */
    public Cursor getProductByProductId(String productId) {

        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_SALESMAN_PRODUCTS + " WHERE " + F_KEY_SS_PRODUCT_ID +
                " = " + productId, null);


    }


    /* Delete Invoice List By Chemist Id */
    public void deleteInvoiceList(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableInvoiceListPayment, Key_ChemistId_InvoiceListPayment + "= '" + chemistId + "'", null);
        database.close();
    }


    /* Delete All Invoices By UserId */
    public void deleteInvoiceByUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableInvoiceListPayment, Key_UserId_InvoiceListPayment + "= '" + userId + "'", null);
        database.close();
    }
    /* Delete All Invoices By UserId */
    public void deleteDailyCollectionByUserId(String str1) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableCollectPaymentSalesman, Key_Flag_PaymentSalesman + "= '" + str1 + "'", null);
        database.close();
    }

    /* Delete Delivery Schedule By UserId */
    public void deleteDeliveryScheduleByUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableDeliveryScheduleSalesman, Key_UserID_DeliverySchedule + "= '" + userId + "'", null);
        database.close();
    }
    /* Delete Delivery Schedule Invoice By UserId */
    public void deleteDeliveryInvoicesByUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableDeliveryScheduleInvoices, Key_UserId_DeliveryScheduleInvoices + "= '" + userId + "'", null);
        database.close();
    }
    /* Chemist List Salesman For Create Order */
    public Cursor getChemistListSalesmanForCreateOrder(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + ChemistListForCreateOrderSalesmanTable + " WHERE " + Key_UserId_ChemistListForCreateOrderSalesman + " = '" + userId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Delete Chemist List Salesman For Create Order */
    public void deleteChemistListSalesmanForCreateOrder(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ChemistListForCreateOrderSalesmanTable, Key_UserId_ChemistListForCreateOrderSalesman + "= '" + userId + "'", null);
        database.close();
    }


    /* Delete Old ChemistList Entries */
    public void deleteRecordFromSalesmanChemistList(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(SalesmanChemistListTable, Key_UserId_SalesmanChemistList + "= '" + userId + "'", null);
        database.close();
    }


    /* Get ChemistList */
    public Cursor getSalesmanChemistList(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + SalesmanChemistListTable + " WHERE " + Key_UserId_SalesmanChemistList + " = '" + userId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
    /* Get CustomerListDeliverySalesman */
    public Cursor getDeliveryScheduleInvoices(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TableDeliveryScheduleInvoices + " WHERE " + Key_UserId_DeliveryScheduleInvoices + " = '" + userId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Get Save Delivery Data */
    public Cursor getSavedDelivery() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableSaveDeliveryOffline, null, null);
        return cursor;
    }


   // COMMNET THIS METHOD 13 AUGUST HARISH and PRAKASH SIR WORKING

   /* *//* Get Save Delivery Data *//*
    public Cursor getSavedDeliveryOffline(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableCollectDeliverySalesman + " WHERE " + Key_UserId_DeliverySalesman +
                " = '" + userid + "'", null);

        return cursor;
    }*/

    /* Get Save Delivery Data */
    public Cursor getSavedDeliveryOffline(String userid,String sDeliveryStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if (sDeliveryStatus.equals(""))
        {
            cursor = db.rawQuery("SELECT * FROM " + TableCollectDeliverySalesman +
                    " WHERE " + Key_UserId_DeliverySalesman +
                    " = '" + userid + "'", null);
        }
        else
        {
            cursor = db.rawQuery("SELECT * FROM " + TableCollectDeliverySalesman +
                            " WHERE " + Key_UserId_DeliverySalesman + " = '" + userid +"' and "
                            + Key_DeliveryStatus_DeliverySalesman + " = '" + sDeliveryStatus + "'" ,
                    null);
        }

        return cursor;
    }




    /* Get Save Delivery Data */
    public Cursor getSavedDeliveryOfflineByDocID(String doc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableCollectDeliverySalesman + " WHERE " + Key_DeliveryId_DeliverySalesman +
                " = '" + doc_id + "'", null);

        return cursor;
    }
    /* Get Save Delivery Data */
    public Cursor getSavedDeliveryInvoicesOffline(String doc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableCollectDeliverySelectedInvoice + " WHERE " + Key_DeliveryId_DeliverySelectedInvoice +
                " = '" + doc_id + "'", null);

        return cursor;
    }


    /* Delete SavedDelivery */
    public void deleteSavedDelivery() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableSaveDeliveryOffline, null, null);
        database.close();
    }


    /* Get Offline Orders By UserId */
    public Cursor getAllOrderByUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TablePlacedOrders + " WHERE " + Key_UserId_PlacedOrder + " = '" + userId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Get Offline Orders By ChemistId */
    public Cursor getAllOrderByChemistId(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TablePlacedOrders + " WHERE " + Key_ChemistId_PlacedOrder + " = '" + chemistId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Get Order Items By OrderNo(DocNo) */
    public Cursor getOrderItemsByOrderNo(String orderNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TablePlacedOrders + " WHERE " + Key_DocNo_PlacedOrder + " = '" + orderNo + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Get Offline Orders By ChemistId In Chemist Section */
    public Cursor getAllOrderByChemistIdChemistSection(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TablePlacedOrders + " WHERE " + Key_UserId_PlacedOrder + " = '" + userId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Delete Order Record By UserId */
    public void deleteOrdersByUserId(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TablePlacedOrders, Key_UserId_PlacedOrder + "= '" + userId + "'", null);
        database.close();
    }


    /* Delete Order Record By UserId In Chemist Section */
    public void deleteOrdersByUserIdChemistSection(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TablePlacedOrders, Key_UserId_PlacedOrder + "= '" + userId + "'", null);
        database.close();
    }


    /* Updates by sumit on 9th April */
    public void deleteOrderByDocNo(String docNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TablePlacedOrders, Key_DocNo_PlacedOrder + " = ?", new String[]{docNo});
        database.close();
    }
    public void deleteLocationDatabyCustomerId(String docNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableForLocation, F_KEY_CC_DOC_NO + " = ?", new String[]{docNo});
        database.close();
    }

    /* Delete Invoice List Delivery */
    public void deleteInvoiceListDelivery(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableInvoiceListDelivery, Key_ChemistId_InvoiceListDelivery + "= '" + chemistId + "'", null);
        database.close();
    }


    /* Get Invoice List Delivery */
    public Cursor getInvoiceListDelivery(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TableInvoiceListDelivery + " WHERE " + Key_ChemistId_InvoiceListDelivery + " = '" + chemistId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Update Invoice List Delivery */
    public void updateInvoiceListDelivery(String invoiceNo, String orderStatus) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Key_OrderStatus_InvoiceListDelivery, orderStatus);
        database.update(TableInvoiceListDelivery, values, Key_InvoiceNo_InvoiceListDelivery + " = ?", new String[]{invoiceNo});
        database.close();
    }

    public void updateLocationstatusChemistList(String chemist,String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHEMIST_LOCATION_FLAG, status);
        database.update(SalesmanChemistListTable, values,Key_ChemistId_SalesmanChemistList + "= '" + chemist + "'", null);
        database.close();
    }

    public void update_into_chemist_cart(String Doc_id, Integer item_count, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(F_KEY_CC_ITEM_COUNT, item_count);
        values.put(F_KEY_CC_ORDER_AMOUNT, amount);


        long id = db.update(TABLE_CHEMIST_CART, values, "DOC_NO='" + Doc_id + "'", null);
        //db.close(); //
    }

    //**join
    public Cursor get_chemist_cart_data(String Doc_id) {
        String buildSQL = "select distinct ccd.DOC_NO,  ccd.*,sp.* FROM " + TABLE_STOCKIST_WISE_PRODUCTS +
                "  sp, " + TABLE_CHEMIST_CART_DETAIL + " ccd " +
                " where   sp.Product_ID=ccd.Product_ID  and ccd.DOC_NO='" + Doc_id + "' order by ccd.DOC_NO";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_saleman_cart_data(String Doc_id) {
        String buildSQL = "select distinct ccd.DOC_NO,  ccd.*,sp.* FROM " + TABLE_SALESMAN_PRODUCTS +
                "  sp, " + TABLE_CHEMIST_CART_DETAIL + " ccd " +
                " where   sp.Product_ID=ccd.Product_ID  and ccd.DOC_NO='" + Doc_id + "' order by ccd.DOC_NO";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(buildSQL, null);
    }


    public Boolean check_saleman_product_availabity(String p_id) {
        Boolean p_availble = false;
        String buildSQL = "select * FROM " + TABLE_SALESMAN_PRODUCTS + " where Itemcode='" + p_id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(buildSQL, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                p_availble = true;
            }
            cursor.close();
            // return row count
        }
        return p_availble;
    }


    public Cursor get_chemist_cart(String Stockist_id) {
        String buildSQL = "select * FROM " + TABLE_CHEMIST_CART + " where Stockist_Client_id=" + Stockist_id;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_legend_data(String Stockist_id) {

        String buildSQL = "select * FROM " + TABLE_LEGEND_MASTER + " where stockistId=" + Stockist_id;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }


    public Cursor delete_legend_data(String Stockist_id) {

        String buildSQL = "delete FROM " + TABLE_LEGEND_MASTER + " where stockistId=" + Stockist_id;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_chemist_cart_detail(String DocId) {

        String buildSQL = "select * FROM " + TABLE_CHEMIST_CART_DETAIL + " where DOC_NO='" + DocId + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_chemist_order_details(String Order_id) {

        String buildSQL = "select  * FROM " + TABLE_CHEMIST_ORDER_DEATILS + "    where OrderNo=" + Order_id;

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }

    public int check_cart_id(String cart_id) {

        String buildSQL = "select * FROM " + TABLE_CHEMIST_CART + "  where CartId='" + cart_id + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(buildSQL, null);
        int rowCount = cursor.getCount();
        //db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    // insert data using transaction and prepared statement
    public void insertInto_call_plan(String call_id, String client_id, String Location, int call_started, String call_duration, int delivery,
                                     int payment, int returns, int order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        try {


            ContentValues values = new ContentValues();
            values.put(F_KEY_CP_CALL_PLAN_ID, call_id); // Name
            values.put(F_KEY_CP_CLIENT_ID, client_id); // Name
            values.put(F_KEY_CP_LOCATION, Location); // Name
            values.put(F_KEY_CP_CALL_START, call_started); // Name
            values.put(F_KEY_CP_CALL_DURATION, call_duration); // Name
            values.put(F_KEY_CP_DELIVERY, delivery); // Name
            values.put(F_KEY_CP_PAYMENT, payment); // Name
            values.put(F_KEY_CP_RETURN, returns); // Name
            values.put(F_KEY_CP_ORDER, order); // Name

            db.insertOrThrow(TABLE_CALL_PLAN, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }

    }


    public void insertInto_Chemist_order_details(String Order_no, String item_srno, String item_name, Double MRP, Integer packsize, Integer Qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        try {


            ContentValues values = new ContentValues();
            values.put(F_KEY_COD_ORDER_NO, Order_no); // Name
            values.put(F_KEY_COD_ITEMSR_NO_, item_srno); // Name
            values.put(F_KEY_COD_MRP, MRP); // Name
            values.put(F_KEY_COD_PACK_SIZE, packsize); // Name
            values.put(F_KEY_COD_QTY, Qty); // Name
            values.put(F_KEY_COD_ITEM_NAME, item_name); // Name

            db.insertOrThrow(TABLE_CHEMIST_ORDER_DEATILS, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }

    }

    public int check_stockist_data(String Stockist_id) {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKIST_WISE_PRODUCTS + " where stockist_id=" + Stockist_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();

        cursor.close();

        // return row count
        return rowCount;
    }

    public Float get_total_order_amount(String Doc_id) {

        Float amount = 0f;
        String countQuery = "SELECT  * FROM " + TABLE_CHEMIST_CART + " where DOC_NO='" + Doc_id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                amount = cursor.getFloat(cursor.getColumnIndex("Amount"));
            }
            cursor.close();

            // return row count

        }
        return amount;
    }


    public Integer get_total_order_item_count(String Doc_id) {

        Integer count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_CHEMIST_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
     /*   if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                count = cursor.getInt(cursor.getColumnIndex(F_KEY_CC_DOC_NO));
            }*/


        count = cursor.getCount();
        cursor.close();

        // return row count

        // }
        return count;
    }


    public Cursor get_stockist_data(String Stockist_id) {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKIST_WISE_PRODUCTS + " where stockist_id=" + Stockist_id + " order by Itemname";
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);
    }

    public Cursor get_stockist_inventory() {
        String countQuery = "SELECT  * FROM " + TABLE_SALESMAN_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);
    }


    public Cursor get_order_json() {
        String countQuery = "SELECT  * FROM " + TABLE_CHEMIST_ORDER_SYNC;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);
    }

    public Cursor get_order_json_on_doc_id(String doc_id) {
        String countQuery = "SELECT  * FROM " + TABLE_CHEMIST_ORDER_SYNC + " where DOC_ID='" + doc_id + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);
    }

    public void delete_chemist_order_json(String p_id) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_CHEMIST_ORDER_SYNC, "DOC_ID = '" + p_id + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }


    /* Delete Chemist Products */
    public void deleteRecordFromChemistProduct(String stockistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ChemistStockistWiseProduct, KEY_STOCKIST_ID_CHEMIST_PRO + "= '" + stockistId + "'", null);
        database.close();
    }


    /* Get Chemist Products */
    public Cursor getChemistProductListByStockist(String stockistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + ChemistStockistWiseProduct + " WHERE " + KEY_STOCKIST_ID_CHEMIST_PRO + " = '" + stockistId + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }


    /* Update Chemist Product List in Chemist Section */
    public void updateProductListChemist(String productId, String itemCode, String itemName, String packSize, String mrp, String rate,
                                         String stock, String type, String mfgCode, String mfgName, String doseFrom, String scheme) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEMCODE_CHEMIST_PRO, itemCode);
        values.put(KEY_ITEMNAME_CHEMIST_PRO, itemName);
        values.put(KEY_PACKSIZE_CHEMIST_PRO, packSize);
        values.put(KEY_MRP_CHEMIST_PRO, mrp);
        values.put(KEY_RATE_CHEMIST_PRO, rate);
        values.put(KEY_STOCK_CHEMIST_PRO, stock);
        values.put(KEY_TYPE_CHEMIST_PRO, type);
        values.put(KEY_MFGCODE_CHEMIST_PRO, mfgCode);
        values.put(KEY_MFGNAME_CHEMIST_PRO, mfgName);
        values.put(KEY_DOSEFORM_CHEMIST_PRO, doseFrom);
        values.put(KEY_SCHEME_CHEMIST_PRO, scheme);
        Log.d("updatedValueChemist", values.toString());
        database.update(ChemistStockistWiseProduct, values, KEY_PRODUCT_ID_CHEMIST_PRO + " = ?", new String[]{productId});
    }


    /* Delete Record From Salesman Product */
    public void deleteRecordFromSalesmanProduct() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SALESMAN_PRODUCTS, null, null);
        database.close();
    }




    /* Get Salesman Product List */
    /*public Cursor getSalesmanProductList(String clientId) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_SALESMAN_PRODUCTS + " WHERE " + F_KEY_SS_STOCKIST_ID + "=" + clientId, null);
        return cursor;
    }*/


    /* Get Pending Payment Invoice List By Chemist Id (Salesman) */
  /*  public Cursor getPendingInvoiceList(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment + " = '" +
                chemistId + "'" + " AND " + Key_BalanceAmount_InvoiceListPayment + " > '0'" + " AND " +
                Key_BalanceAmount_InvoiceListPayment + " < '" + Key_BillAmount_InvoiceListPayment + "'" + " ORDER BY " +
                Key_InvoiceDate_InvoiceListPayment + " ASC";
        return database.rawQuery(query, null);
    }*/


    /* Get Pending Payment Invoice List By Chemist Id (Salesman) */
    public Cursor getPendingInvoiceList(String chemistId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment + " = '" +
                chemistId + "'" + " AND " + Key_BalanceAmount_InvoiceListPayment + " > '0'" + " AND " +
                Key_BalanceAmount_InvoiceListPayment + " < '" + Key_BillAmount_InvoiceListPayment + "'" + " ORDER BY " +
                Key_InvoiceDate_InvoiceListPayment + " ASC";
        return database.rawQuery(query, null);
    }
    public Cursor getPendingInvoiceList1(String chemistId, String invoiceFlag) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_ChemistId_InvoiceListPayment + " = '" +
                chemistId + "'" + " AND " + Key_BalanceAmount_InvoiceListPayment + " > '0'" + " AND " +
                Key_BalanceAmount_InvoiceListPayment + " < '" + Key_BillAmount_InvoiceListPayment + "'" +" AND "+ Key_PaymentFlag_InvoiceListPayment + " = "+ invoiceFlag +" ORDER BY " +
                Key_InvoiceDate_InvoiceListPayment + " ASC";
        return database.rawQuery(query, null);
    }
    /* Get Salesman Product List */
    public Cursor getSalesmanProductList() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_SALESMAN_PRODUCTS, null);
        return cursor;
    }


    /* Update Product List In Salesman Section */
    public void updateProductsSalesman(String productId, String itemCode, String itemName, String packSize, String mrp, String rate,
                                       String stock, String mfgCode, String mfgName, String doseFrom, String scheme, String HalfScheme, String PercentScheme, String LegendName, String LegendColor, String BoxSize,String type, String stockist_id) {
       // SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(F_KEY_SS_ITEMCODE, itemCode);
        values.put(F_KEY_SS_ITEMNAME, itemName);
        values.put(F_KEY_SS_PACKSIZE, packSize);
        values.put(F_KEY_SS_MRP, mrp);
        values.put(F_KEY_SS_RATE, rate);
        values.put(F_KEY_SS_STOCK, stock);
        values.put(F_KEY_SS_MFGCODE, mfgCode);
        values.put(F_KEY_SS_MFGNAME, mfgName);
        values.put(F_KEY_SS_DOSEFORM, doseFrom);
        values.put(F_KEY_SS_SCHEME, scheme);
//        values.put(F_KEY_SS_DOSEFORM, doseFrom);
//        values.put(F_KEY_SS_SCHEME, scheme);
        values.put(key_product_halfscheme, HalfScheme);
        values.put(key_product_percentscheme, PercentScheme);
        values.put(key_product_legendname, LegendName);
        values.put(key_product_legendcolor, LegendColor);
        values.put(key_product_BoxSize, BoxSize);
        values.put(F_KEY_SS_TYPE, type);
        values.put(F_KEY_SP_STOCKIST_ID, stockist_id);
        mydb.update(TABLE_SALESMAN_PRODUCTS, values, F_KEY_SS_PRODUCT_ID + " = ?", new String[]{productId});
        //database.close();
        Log.d("updatedVales", values.toString());
        Log.d("updatedProductId", productId);
    }

//    public void updateFlagCollectInvoicesSalesman(String invoiceno, String flag) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        String query = "UPDATE " + TableDeliveryScheduleInvoices + " SET " + Key_DeliveryFlag_DeliveryScheduleInvoices + " = " +
//                "'" + flag + "'" + " WHERE " +
//                Key_InvoiceNo_DeliveryScheduleInvoices + " = '" + invoiceno + "'";
//
//        Log.d("updatedQuery", query);
//        database.execSQL(query);
//        database.close();
//    }

    public void updateFlagCollectInvoicesSalesman(String invoiceno, String flag) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableDeliveryScheduleInvoices + " SET " + Key_DeliveryFlag_DeliveryScheduleInvoices + " = " +
                "'" + flag + "'" + " WHERE " +
                Key_DeliveryId_DeliveryScheduleInvoices + " = '" + invoiceno + "'";

        Log.d("updatedQuery", query);
        database.execSQL(query);
        database.close();
    }

    public void updateFlagDescriptionInvoices(String invoiceno, String Description) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableDeliveryScheduleInvoices + " SET " + Key_DeliveryDescription_DeliveryScheduleInvoices + " = " +
                "'" + Description + "'" + " WHERE " +
                Key_DeliveryId_DeliveryScheduleInvoices + " = '" + invoiceno + "'";

        Log.d("updatedQuery", query);
        database.execSQL(query);
        database.close();
    }

    public void updateFlagDeliveryDescription(String invoiceno, String Description) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + TableDeliveryScheduleInvoices + " SET " + Key_DeliveryDescription_DeliveryScheduleInvoices + " = " +
                "'" + Description + "'" + " WHERE " +
                Key_DeliveryId_DeliveryScheduleInvoices + " = '" + invoiceno + "'";

        Log.d("updatedQuery", query);
        database.execSQL(query);
        database.close();
    }

    public void deleteDeliverySelectedInvoiceByDocid(String docid) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableCollectDeliverySelectedInvoice, Key_DeliveryId_DeliverySelectedInvoice + " = ?", new String[]{docid});
        database.close();
    }

    public void deleteDeliverySalesmanByDocid(String docid) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TableCollectDeliverySalesman, Key_DeliveryId_DeliverySalesman + " = ?", new String[]{docid});
        database.close();
    }

    /* Get Count of Salesman Product List */
    /*public int getProfilesCount() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_SALESMAN_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorToday = db.rawQuery (countQuery, null) ;
        cursorToday.moveToFirst();
        int count = cursorToday.getInt(0);
        cursorToday.close();
        return count;
    }*/


    public void delete_chemist_Cart(String doc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_CHEMIST_CART, "DOC_NO = '" + doc_id + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public void delete_chemist_Cart_Details(String doc_id) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_CHEMIST_CART_DETAIL, "DOC_NO = '" + doc_id + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public void delete_product_from_cart_chemist_Cart_Details(String doc_id, String itemno) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_CHEMIST_CART_DETAIL, "DOC_NO = '" + doc_id + "'   and Doc_item_No='" + itemno + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }


    public void delete_product_from_cart_chemist_Cart_Details(String createdOn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_CHEMIST_CART_DETAIL, " Createdon = '" + createdOn + "'  ", null);
            // int i=db.delete(TABLE_CHEMIST_CART_DETAIL, "DOC_NO = '" + doc_id+"'", null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public void delete_stockist_inventory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int i = db.delete(TABLE_SALESMAN_PRODUCTS, "", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }


    /* Get All Invoice Daily Collection */
    public Cursor getAllInvoiceList() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " WHERE " + Key_InvoiceDate_InvoiceListPayment
                + " = '" + Constant.currentDate + "' ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
        // return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);

    }
    public Cursor getTableCollectPDCPaymentSalesman(String chemistId) {
        String mode = "Cheque";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman + " WHERE " +
                Key_ChemistId_PaymentSalesman + " = '" + chemistId + "'" + " AND " + Key_PaymentMode_PaymentSalesman + " = 'Cheque'", null);
        // return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);

    }
    public Cursor getTableCollectPaymentSalesman() {
        SQLiteDatabase database = this.getReadableDatabase();
   //     return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman , null);
        // return database.rawQuery("SELECT * FROM " + TableInvoiceListPayment + " ORDER BY " + Key_InvoiceDate_InvoiceListPayment + " ASC", null);
        return database.rawQuery("SELECT * FROM " + TableCollectPaymentSalesman + " ORDER BY " + Key_PaymentDate_PaymentSalesman + " DESC", null);
    }

    public Cursor get_stockist_id_on_medicine_name(String medicine_name) {
        String countQuery = "SELECT  distinct Stockist_id FROM " + TABLE_STOCKIST_WISE_PRODUCTS + " where Itemname like '%" + medicine_name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(countQuery, null);
    }

  /*  public Cursor generate_order_data()
    {

    }*/


}