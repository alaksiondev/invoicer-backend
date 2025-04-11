import xml.etree.ElementTree as ET

def parse_file(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()

    counters = root.findall("counter")

    result = {}

    for count in counters:
        type_ = count.get("type")

        if type_ == "LINE" or type_ == "BRANCH":
            missed = int(count.get("missed"))
            covered = int(count.get("covered"))
            total = missed + covered
            percent = round((covered / total * 100), 2) if total > 0 else 0.0
            result[type_] = {
                "total": total,
                "missed": missed,
                "covered": covered,
                "coverage": percent
            }
    return result

def compare_files():
    branchCoverage = parse_file("branch-report.xml")
    mainCoverage = parse_file("main-coverage.xml")

    lineDiff = branchCoverage["LINE"]["coverage"] - mainCoverage["LINE"]["coverage"]
    branchDiff = branchCoverage["BRANCH"]["coverage"] - mainCoverage["BRANCH"]["coverage"]