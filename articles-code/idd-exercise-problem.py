from collections import Counter
from bs4 import BeautifulSoup
from urllib.parse import urlparse, quote_plus
import itertools
import requests

class ScraperException(Exception):
    pass

class GoogleScraper(object): 
    def scrape(self, query): 
        try: 
            query_normalized = quote_plus(query)
            r = requests.get('https://www.google.com/search?q={}'.format(query_normalized))
            soup = BeautifulSoup(r.text, "html.parser") 
            domains = [] 
            for item in soup.find_all('div', attrs={'class' : 's'}): 
                cite = item.find('cite') 
                url = cite.text 
                if not url.startswith('http'): 
                    url = "http://{}".format(url) 
                href = urlparse(url) 
                domains.append(href.netloc) 
            return domains 
        except Exception as e: 
            raise ScraperException(e)

class QueryService(object):
    def __init__(self, scraper):
        self.scraper = scraper

    def run(self, query):
        try:
            scraper_result = self.scraper.scrape(query)
            return (True, scraper_result)
        except ScraperException as e:
            return (False, query, e)


def handle_ok(results):
    domains = itertools.chain(*map(lambda x: x[1], results))
    counts = Counter(domains)
    print("Most common domains: ")
    print(counts.most_common(10))


def handle_nok(results):
    failed_queries = list(map(lambda x: x[1], results))
    if len(failed_queries) > 0:
        print("The following querie have failed: \n{}".format("\n".join(failed_queries)))

def handle(results):
    ok_results = filter(lambda x: x[0] is True, results)
    handle_ok(ok_results)
    nok_results = filter(lambda x: x[0] is False, results)
    handle_nok(nok_results)


def main(query_source, query_service, handler):
    results = []
    for query in query_source:
        result = query_service.run(query)
        results.append(result)
    handler(results)


if __name__ == "__main__":
    query_source = ["test", "microsoft", "google"]
    query_service = QueryService(GoogleScraper())

    main(query_source, query_service, handle)

def test_main():
    query_source = ["test1", "test2", "test3"]
    class TestQueryService(object):
        def run(self, query: str):
            if query == "test3":
                return (True, ["domain3"])
            elif query == "test2":
                return (True, ["domain3", "domain2"])
            elif query == "test1":
                return (True, ["domain3", "domain2", "domain1"])
            else:
                return (False, query, None)

    query_service = TestQueryService()
    expected_frequencies = {"domain1": 1, "domain2": 2, "domain3": 3}
    actual_frequencies = None

    def test_handler(results):
        nonlocal actual_frequencies
        domains = itertools.chain(*map(lambda x: x[1], results))
        actual_frequencies = Counter(domains)
    
    main(query_source, query_service, test_handler)

    assert actual_frequencies == expected_frequencies
