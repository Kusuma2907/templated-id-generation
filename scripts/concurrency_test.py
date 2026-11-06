import json
import sys
from concurrent.futures import ThreadPoolExecutor, as_completed
from urllib.request import Request, urlopen


BASE_URL = "http://localhost:8080"
TEMPLATE_ID = "PG_RECEIPT"
CITY = "AMR"

WORKERS = 20
REQUESTS = 100


def issue_one(index):
    payload = json.dumps({
        "templateId": TEMPLATE_ID,
        "city": CITY
    }).encode("utf-8")

    request = Request(
        f"{BASE_URL}/api/v1/ids",
        data=payload,
        headers={
            "Content-Type": "application/json"
        },
        method="POST"
    )

    with urlopen(request, timeout=30) as response:
        body = json.loads(
            response.read().decode("utf-8")
        )

        if response.status != 201:
            raise RuntimeError(
                f"Request {index} failed: {body}"
            )

        return body["id"]


def main():

    print(
        f"Running {REQUESTS} parallel requests "
        f"using {WORKERS} workers..."
    )

    ids = []

    with ThreadPoolExecutor(
            max_workers=WORKERS
    ) as executor:

        futures = [
            executor.submit(
                issue_one,
                index
            )
            for index in range(REQUESTS)
        ]

        for future in as_completed(futures):
            ids.append(future.result())

    unique_ids = set(ids)

    print(f"Total IDs:  {len(ids)}")
    print(f"Unique IDs: {len(unique_ids)}")

    if len(ids) != len(unique_ids):

        print("FAIL: duplicate IDs detected")
        sys.exit(1)

    print("PASS: zero duplicate IDs detected")


if __name__ == "__main__":
    main()