package po41.Martynchik.wdad.learn.xml;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XmlTask {
    private String path = "src/PO41/Martynchik/wdad/learn/xml/housekeeper.xml";
    private Document housekeeper;
    //Куча констант с именами XML-тегов и атрибутов
    private static final String COLDWATER_TAG_NAME = "coldwater";
    private static final String HOTWATER_ATTRIBUTE_NAME = "hotwater";
    private static final String ELECTRICITY_ATTRIBUTE_NAME = "electricity";
    private static final String GAS_ATTRIBUTE_NAME = "gas";
    private static final String STREET_ATTRIBUTE_NAME = "street";
    private static final String NUMBER_ATTRIBUTE_NAME = "number";
    private static final String PERSONS_QUANTITY_ATTRIBUTE_NAME = "personsquantity";
    private static final String AREA_ATTRIBUTE_NAME = "area";
    private static final String YEAR_ATTRIBUTE_NAME = "year";
    private static final String MONTH_ATTRIBUTE_NAME = "month";
    private static final String BUILDING_TAG_NAME = "building";
    private static final String TARIFF_TAG_NAME = "tariffs";
    private static final String REGISTRATION_TAG_NAME = "registration";
    //Тарифы на услуги
    private int coldWaterTariff;
    private int hotWaterTariff;
    private int electricityTariff;
    private int gasTariff;

    private class Registrations {
        int prevColdWaterRegistration;
        int lastColdWaterRegistration;
        int prevHotWaterRegistration;
        int lastHotWaterRegistration;
        int prevElectricityRegistration;
        int lastElectricityRegistration;
        int prevGasRegistration;
        int lastGasRegistration;

        //Нужен для того, чтобы раскидать показания по отдельным полям
        Registrations(Node prevRegistration, Node lastRegistration) {
            writePrevRegistration(prevRegistration);
            writeLastRegistration(lastRegistration);
        }
        //China coding style
        private void writePrevRegistration(Node prevRegistration) {
            NodeList meterReadings = prevRegistration.getChildNodes();
            int meterReadingsLength = meterReadings.getLength();
            for (int i = 0; i < meterReadingsLength; i++) {
                switch (meterReadings.item(i).getNodeName()) {
                    case COLDWATER_TAG_NAME:
                        prevColdWaterRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case HOTWATER_ATTRIBUTE_NAME:
                        prevHotWaterRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case ELECTRICITY_ATTRIBUTE_NAME:
                        prevElectricityRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case GAS_ATTRIBUTE_NAME:
                        prevGasRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                }
            }
        }
        private void writeLastRegistration(Node lastRegistration) {
            NodeList meterReadings = lastRegistration.getChildNodes();
            int meterReadingsLength = meterReadings.getLength();
            for (int i = 0; i < meterReadingsLength; i++) {
                switch (meterReadings.item(i).getNodeName()) {
                    case COLDWATER_TAG_NAME:
                        lastColdWaterRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case HOTWATER_ATTRIBUTE_NAME:
                        lastHotWaterRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case ELECTRICITY_ATTRIBUTE_NAME:
                        lastElectricityRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                    case GAS_ATTRIBUTE_NAME:
                        lastGasRegistration = Integer.valueOf(meterReadings.item(i).getTextContent());
                        break;
                }
            }
        }
    }

    public XmlTask() throws IOException, ParserConfigurationException, SAXException {
        createDocument();   //Создаем дерево
    }

    /**Создает дерево DOM*/
    private void createDocument() throws IOException, ParserConfigurationException, SAXException {
        File housekeeper = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true); //Важная штука. С ней парсер игнорируем пробелы.
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.housekeeper = builder.parse(housekeeper);
    }

    /**Записывает тарифы из XML в соответствующие переменные*/
    private void updateTariffs() {
        NamedNodeMap attributes = housekeeper.getElementsByTagName(TARIFF_TAG_NAME).item(0).getAttributes();
        coldWaterTariff = Integer.valueOf(attributes.getNamedItem(COLDWATER_TAG_NAME).getNodeValue());
        hotWaterTariff = Integer.valueOf(attributes.getNamedItem(HOTWATER_ATTRIBUTE_NAME).getNodeValue());
        electricityTariff = Integer.valueOf(attributes.getNamedItem(ELECTRICITY_ATTRIBUTE_NAME).getNodeValue());
        gasTariff = Integer.valueOf(attributes.getNamedItem(GAS_ATTRIBUTE_NAME).getNodeValue());
    }

    /**Возвращает сумму, которую должен заплатить абонент*/
    public int getBill (String requiredStreet, int requiredBuildingNumber, int requiredFlatNumber) {
        int bill = 0;   //Если что-то пойдет не так, вернем этот ноль
        NodeList buildings = housekeeper.getElementsByTagName(BUILDING_TAG_NAME);  //Получаем все домики
        NodeList flats = getFlatsFromBuilding(buildings, requiredStreet, requiredBuildingNumber);   //Получаем все квартиры из нужного нам здания
        NodeList allRegistrations = getRegistrationsFromFlat(flats, requiredFlatNumber); //Получаем все показания из нужной квартиры
        Registrations actualRegistrations = getActualRegistrations(allRegistrations);   //Получаем актуальные данные (последние и предпоследние)
        bill = calcBill(actualRegistrations);   //Формируем счет на основе показаний
        return bill;
    }

    /**Возвращает здание по его номеру и названию улицы*/
    private NodeList getFlatsFromBuilding(NodeList buildings, String requiredStreet, int requiredBuildingNumber){
        NodeList flats = null;
        int buildingsLength = buildings.getLength();
        for (int i = 0; i < buildingsLength; i++) {
            NamedNodeMap buildingAttributes = buildings.item(i).getAttributes();
            String buildingStreet = buildingAttributes.getNamedItem(STREET_ATTRIBUTE_NAME).getNodeValue();  //получаем улицу, на которой стоит дом
            int buildingNumber = Integer.valueOf(buildingAttributes.getNamedItem(NUMBER_ATTRIBUTE_NAME).getNodeValue()); //получаем номер дома
            if (buildingStreet.equalsIgnoreCase(requiredStreet) && buildingNumber == requiredBuildingNumber) {
                flats = buildings.item(i).getChildNodes();
                return flats;
            }
        }
        return flats;
    }

    /**Возвращает показания для требуемой квартиры*/
    private NodeList getRegistrationsFromFlat(NodeList flats, int requiredFlatNumber) {
        NodeList registrations = null;
        int flatsLength = flats.getLength();
        for (int i = 0; i < flatsLength; i++) {
            NamedNodeMap flatsAttributes = flats.item(i).getAttributes();
            int flatNumber = Integer.valueOf(flatsAttributes.getNamedItem(NUMBER_ATTRIBUTE_NAME).getNodeValue());
            if (flats.item(i).getNodeName().equals("flat") && flatNumber == requiredFlatNumber) {
                registrations = flats.item(i).getChildNodes();
                return registrations;
            }
        }
        return registrations;
    }

    /**Возвращает показания за последний и предыдущий месяц*/
    private Registrations getActualRegistrations(NodeList allRegistrations) {
        Node lastRegistration = null;
        Node prevRegistration = null;
        int lastYear = 0;
        int lastMonth = 0;
        int registrationsLength = allRegistrations.getLength();
        for (int i = 0; i < registrationsLength; i++) {
            NamedNodeMap registrationsAttributes = allRegistrations.item(i).getAttributes();
            int year = Integer.valueOf(registrationsAttributes.getNamedItem(YEAR_ATTRIBUTE_NAME).getNodeValue());
            int month = Integer.valueOf(registrationsAttributes.getNamedItem(MONTH_ATTRIBUTE_NAME).getNodeValue());
            String nodeName = allRegistrations.item(i).getNodeName();
            if (nodeName.equals("registration")) {
                if (year > lastYear) {
                    prevRegistration = lastRegistration;
                    lastRegistration = allRegistrations.item(i);  //Если год больше последнего найденного, то это однозначно новые показания
                    lastYear = year;
                    lastMonth = Integer.valueOf(lastRegistration.getAttributes().getNamedItem(MONTH_ATTRIBUTE_NAME).getNodeValue());
                }
                if (year == lastYear && month > lastMonth) {
                    prevRegistration = lastRegistration;
                    lastRegistration = allRegistrations.item(i); //Если год тот же, но месяц новый, то это более свежие показания
                    lastMonth = month;
                }
            }
        }
        Registrations requiredRegistrations = new Registrations(prevRegistration, lastRegistration);
        return requiredRegistrations;
    }

    /**Подсчитывает показания, умножает на тариф и возвращает количество денег, которое нужно заплатить*/
    private int calcBill(Registrations registrations) {
        updateTariffs();
        int coldWaterUsed = registrations.lastColdWaterRegistration - registrations.prevColdWaterRegistration;
        int hotWaterUsed = registrations.lastHotWaterRegistration - registrations.prevHotWaterRegistration;
        int electricityUsed = registrations.lastElectricityRegistration - registrations.prevElectricityRegistration;
        int gasUsed = registrations.lastGasRegistration - registrations.lastGasRegistration;
        int bill = (coldWaterUsed * coldWaterTariff + hotWaterUsed * hotWaterTariff + electricityUsed * electricityTariff + gasUsed * gasTariff);
        return bill;
    }

    /**Изменяет стоимость заданной единицы показания счетчика*/
    public void setTariff (String name, int value) throws IOException, TransformerException {
        NodeList tariffs = housekeeper.getElementsByTagName(TARIFF_TAG_NAME); //Получаем все элементы с тегом "tariff"
        NamedNodeMap attributes = tariffs.item(0).getAttributes();  //А такой у нас всего один
        attributes.getNamedItem(name).setNodeValue(String.valueOf(value));
        rewriteDocument();
    }

    /**Перезаписывает документ*/
    private void rewriteDocument() throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource domSource = new DOMSource(housekeeper);
        StreamResult streamResult = new StreamResult(new File(path));
        transformer.transform(domSource, streamResult);
    }

    /**Добавляет показания счетчиков к заданной квартире в заданный период*/
    public void addRegistration (String street, int buildingNumber, int flatNumber, int year, int month, int coldWaterValue, int hotWaterValue, int electricityValue, int gasValue) throws IOException, TransformerException {
        NodeList buildings = housekeeper.getElementsByTagName(BUILDING_TAG_NAME); //Получаем все домики
        NodeList flats = getFlatsFromBuilding(buildings, street, buildingNumber); //Получаем все квартирки из нужного домика
        int flatsLength = flats.getLength(); //Сколько-сколько у нас там квартирок?
        for (int i = 0; i < flatsLength; i++) { //Будем перебирать квартирки, пока не найдем нужную
            NamedNodeMap attributes = flats.item(i).getAttributes();
            int number = Integer.valueOf(attributes.getNamedItem(NUMBER_ATTRIBUTE_NAME).getNodeValue());
            if (number == flatNumber) {
                Node newRegistration = housekeeper.createElement(REGISTRATION_TAG_NAME);    //Создаем новую запись с показаниями
                ((Element)newRegistration).setAttribute(YEAR_ATTRIBUTE_NAME, String.valueOf(year)); //Заполняем входными данными
                ((Element)newRegistration).setAttribute(MONTH_ATTRIBUTE_NAME, String.valueOf(month));

                Node coldWater = housekeeper.createElement(COLDWATER_TAG_NAME);
                coldWater.setTextContent(String.valueOf(coldWaterValue));
                Node hotWater = housekeeper.createElement(HOTWATER_ATTRIBUTE_NAME);
                hotWater.setTextContent(String.valueOf(hotWaterValue));
                Node electricity = housekeeper.createElement(ELECTRICITY_ATTRIBUTE_NAME);
                electricity.setTextContent(String.valueOf(electricityValue));
                Node gas = housekeeper.createElement(GAS_ATTRIBUTE_NAME);
                gas.setTextContent(String.valueOf(gasValue));

                newRegistration.appendChild(coldWater); //"Загружаем" наши показания
                newRegistration.appendChild(hotWater);
                newRegistration.appendChild(electricity);
                newRegistration.appendChild(gas);

                flats.item(i).appendChild(newRegistration); //"Загружаем" на еще один "уровень" вверх
                rewriteDocument();
            }
        }
    }
}
